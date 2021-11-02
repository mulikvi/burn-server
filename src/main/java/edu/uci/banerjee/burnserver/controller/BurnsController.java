package edu.uci.banerjee.burnserver.controller;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import edu.uci.banerjee.burnserver.model.Fire;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import edu.uci.banerjee.burnserver.services.DataIngestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
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

  private List<Record> readRecords(InputStream data) {
    CsvParserSettings csvSettings = new CsvParserSettings();
    csvSettings.setHeaderExtractionEnabled(true);
    CsvParser parser = new CsvParser(csvSettings);
    return parser.parseAllRecords(data);
  }
}
