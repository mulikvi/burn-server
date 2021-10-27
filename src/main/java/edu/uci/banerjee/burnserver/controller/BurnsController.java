package edu.uci.banerjee.burnserver.controller;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import edu.uci.banerjee.burnserver.model.Fire;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@Slf4j
public class BurnsController {
  private final FiresRepo repo;

  public BurnsController(FiresRepo repo) {
    this.repo = repo;
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

    int numRecords = saveFires(readRecords(file.getInputStream()));

    log.info("Saved {} Fires.", numRecords);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PostMapping("/load")
  public ResponseEntity<String> loadBurnData(@RequestBody String csv) throws Exception {
    log.info("Received New Dataset.");

    int numRecords = saveFires(readRecords(new ByteArrayInputStream(csv.getBytes())));

    log.info("Saved {} Fires.", numRecords);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  private List<Record> readRecords(InputStream data) {
    CsvParserSettings csvSettings = new CsvParserSettings();
    csvSettings.setHeaderExtractionEnabled(true);
    CsvParser parser = new CsvParser(csvSettings);
    return parser.parseAllRecords(data);
  }

  private int saveFires(List<Record> records) {
    List<Fire> burns = new ArrayList<>();

    records.forEach(
        record -> {
          Fire fire = new Fire();
          fire.setYear(Integer.parseInt(record.getString("year")));
          try {
            fire.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(record.getString("date")));
          } catch (ParseException e) {
            e.printStackTrace();
          }
          fire.setName(record.getString("name"));
          fire.setAcres(Double.parseDouble(record.getString("acres")));
          fire.setLatitude(Double.parseDouble(record.getString("latitude")));
          fire.setLongitude(Double.parseDouble(record.getString("longitude")));
          fire.setBurnType(record.getString("burn_type"));
          fire.setCounty(record.getString("county"));
          fire.setSource(record.getString("source"));
          burns.add(fire);
        });
    repo.saveAll(burns);

    return burns.size();
  }
}
