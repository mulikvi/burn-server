package edu.uci.banerjee.burnserver.controller;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import edu.uci.banerjee.burnserver.model.Fire;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import edu.uci.banerjee.burnserver.model.Statistics;
import edu.uci.banerjee.burnserver.services.DataIngestService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class BurnsController {
  private final FiresRepo repo;
  private final DataIngestService dataIngestService;
  private final ExecutorService pool;

  public BurnsController(FiresRepo repo, DataIngestService dataIngestService) {
    this.repo = repo;
    this.dataIngestService = dataIngestService;

    this.pool = Executors.newCachedThreadPool();
  }

  @GetMapping("/fires")
  public List<Fire> getAll() {
    return repo.findAll();
  }

  @PostMapping("/load/file")
  public ResponseEntity<String> loadBurnData(@RequestParam("file") MultipartFile file)
      throws Exception {
    log.info("Received New Dataset.");

    if (file.isEmpty()) {
      log.info("File Received Empty.");
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    int numRecords = dataIngestService.saveFires(readRecords(file.getInputStream()));

    log.info("Saved {} Fires.", numRecords);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/load")
  public ResponseEntity<String> loadBurnData(@RequestBody String csv) {
    log.info("Received New Dataset.");

    pool.submit(
        () -> {
          int numRecords =
              dataIngestService.saveFires(readRecords(new ByteArrayInputStream(csv.getBytes())));

          log.info("Saved {} Fires.", numRecords);
        });

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @GetMapping("query")
  public ResponseEntity<Resp> queryFires(
      @RequestParam(required = false) String source,
      @RequestParam(required = false) String countyUnitId,
      @RequestParam(required = false) String county,
      @RequestParam(required = false) Double minAcres,
      @RequestParam(required = false) Double maxAcres,
      @RequestParam(required = false) String burnType,
      @RequestParam(required = false) String treatmentType,
      @RequestParam(required = false) Integer startYear,
      @RequestParam(required = false) Integer endYear,
      @RequestParam(required = false) Integer startMonth,
      @RequestParam(required = false) Integer endMonth,
      @RequestParam(required = false) String owner,
      @RequestParam(required = false) Boolean escaped,
      @RequestParam(required = false) Double minSeverity,
      @RequestParam(required = false) Double maxSeverity) {

    log.debug("Query against all features.");

    final var fires =
        repo.findByAllParams(
            source,
            countyUnitId,
            county,
            minAcres,
            maxAcres,
            burnType,
            treatmentType,
            startYear,
            endYear,
            startMonth,
            endMonth,
            owner,
            escaped);
    final var resp = new Resp(new EmbeddedData(fires));

    log.debug("Discovered {}.", resp);

    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  @GetMapping("statistics")
  public Statistics fireStatistics(
      @RequestParam(required = false) String source,
      @RequestParam(required = false) String countyUnitId,
      @RequestParam(required = false) String county,
      @RequestParam(required = false) Double minAcres,
      @RequestParam(required = false) Double maxAcres,
      @RequestParam(required = false) String burnType,
      @RequestParam(required = false) String treatmentType,
      @RequestParam(required = false) Integer startYear,
      @RequestParam(required = false) Integer endYear,
      @RequestParam(required = false) Integer startMonth,
      @RequestParam(required = false) Integer endMonth,
      @RequestParam(required = false) String owner,
      @RequestParam(required = false) Boolean escaped,
      @RequestParam(required = false) Double minSeverity,
      @RequestParam(required = false) Double maxSeverity) {

    log.debug("Calculating fire statistics.");

    final var fireStats =
        repo.filterStatistics(
            source,
            countyUnitId,
            county,
            minAcres,
            maxAcres,
            burnType,
            treatmentType,
            startYear,
            endYear,
            startMonth,
            endMonth,
            owner,
            escaped);

    String[] stats = fireStats.split(",");
    Integer count = Integer.parseInt(stats[0]);
    Double size = Double.parseDouble(stats[1]);
    Integer minYear = Integer.parseInt(stats[2]);
    Integer maxYear = Integer.parseInt(stats[3]);
    Double minSize = Double.parseDouble(stats[4]);
    Double maxSize = Double.parseDouble(stats[5]);
    Statistics fireStatistics = new Statistics(count, size, minYear, maxYear, minSize, maxSize);
    return fireStatistics;
  }

  private List<Record> readRecords(InputStream data) {
    CsvParserSettings csvSettings = new CsvParserSettings();
    csvSettings.setHeaderExtractionEnabled(true);
    CsvParser parser = new CsvParser(csvSettings);
    return parser.parseAllRecords(data);
  }

  @Data
  private static class Resp implements Serializable {
    private final EmbeddedData _embedded;
  }

  @Data
  private static class EmbeddedData implements Serializable {
    private final List<Fire> fires;
  }
}
