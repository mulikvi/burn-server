package edu.uci.banerjee.burnserver.services;

import com.univocity.parsers.common.record.Record;
import edu.uci.banerjee.burnserver.model.Fire;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

@Service
@Slf4j
public class DataIngestService {
  private final FiresRepo repo;
  private final LandOwnershipService landOwnershipService;

  public DataIngestService(FiresRepo repo, LandOwnershipService landOwnershipService) {
    this.repo = repo;
    this.landOwnershipService = landOwnershipService;
  }

  public int saveFires(List<Record> records) {
    log.debug("Saving new fires records.");
    final var burns = records.parallelStream().map(this::createFire).collect(toUnmodifiableList());
    repo.saveAll(burns);

    return burns.size();
  }

  private Fire createFire(final Record fireRecord) {
    log.debug("Ingesting record {}", fireRecord);

    final Fire fire = new Fire();
    fire.setYear(Integer.parseInt(fireRecord.getString("year")));
    try {
      fire.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(fireRecord.getString("date")));
    } catch (ParseException e) {
      log.warn("Date is Invalid.");
    }
    fire.setName(fireRecord.getString("name"));
    fire.setAcres(Double.parseDouble(fireRecord.getString("acres")));
    fire.setLatitude(Double.parseDouble(fireRecord.getString("latitude")));
    fire.setLongitude(Double.parseDouble(fireRecord.getString("longitude")));
    fire.setBurnType(fireRecord.getString("burn_type"));
    fire.setCounty(fireRecord.getString("county"));
    fire.setSource(fireRecord.getString("source"));
    fire.setOwner(
        landOwnershipService.getOwnershipFromCoordinate(fire.getLatitude(), fire.getLongitude()));

    return fire;
  }
}
