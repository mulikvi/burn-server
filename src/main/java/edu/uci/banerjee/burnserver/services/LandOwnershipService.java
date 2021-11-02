package edu.uci.banerjee.burnserver.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
@Slf4j
public class LandOwnershipService {
  // Utilizes data from
  // https://hub.arcgis.com/datasets/CALFIRE-Forestry::california-land-ownership/about
  // Read up on usage at
  // https://hub.arcgis.com/datasets/CALFIRE-Forestry::california-land-ownership/api

  private static final String API_PREFIX =
      "https://egis.fire.ca.gov/arcgis/rest/services/FRAP/ownership/Feature"
          + "Server/0/query?where=1%3D1&outFields=OWN_LEVEL,OWN_AGENCY,OWN_GROUP&geometry=";
  private static final String API_SUFFIX =
      "&geometryType=esriGeometryPoint&inSR=4326&spatialRel=esriSpatial"
          + "RelIntersects&returnGeometry=false&outSR=4326&f=json";
  private static final String PRIVATE_LAND = "Private";
  private final JsonMapper json;

  public LandOwnershipService() {
    this.json = new JsonMapper();
  }

  public String getOwnershipFromCoordinate(@NonNull final Double lat, @NonNull final Double lon) {
    try {
      log.debug("Called Ownership for Coordinate {} {}", lat, lon);
      HttpHeaders headers = new HttpHeaders();
      headers.setAccept(List.of(MediaType.APPLICATION_JSON));
      final var queryUrl = buildQueryUrl(lat, lon);
      final var response = get(queryUrl);

      if (response.getFeatures() == null
          || (response.getFeatures() != null && response.getFeatures().isEmpty()))
        return PRIVATE_LAND;
      else {
        if (response.getFeatures().size() > 1) log.warn("Received more than one owner of land.");
        return response.getFeatures().get(0).getAttributes().getOwnLevel();
      }
    } catch (RestClientException | IOException ex) {
      log.error("ESRI Get called ended in exception ", ex);
      return "";
    }
  }

  private String buildQueryUrl(@NonNull final Double lat, @NonNull final Double lon) {
    final var latString = "%2C" + lat;
    final var lonString = "%2C" + lon;
    return String.format(
        "%s%s%s%s%s%s", API_PREFIX, lon, latString, lonString, latString, API_SUFFIX);
  }

  private EsriApiResponse get(@NonNull final String queryUrl) throws IOException {
    StringBuilder result = new StringBuilder();
    URL url = new URL(queryUrl);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
      for (String line; (line = reader.readLine()) != null; ) {
        result.append(line);
      }
    }

    return json.readValue(result.toString(), EsriApiResponse.class);
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class EsriApiResponse implements Serializable {
    private List<EsriApiFeature> features;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class EsriApiFeature implements Serializable {
    private EsriApiAttributes attributes;
  }

  @Data
  @JsonIgnoreProperties(ignoreUnknown = true)
  private static class EsriApiAttributes implements Serializable {
    @JsonProperty("OWN_LEVEL")
    private String ownLevel;

    @JsonProperty("OWN_AGENCY")
    private String ownAgency;

    @JsonProperty("OWN_GROUP")
    private String ownGroup;
  }
}
