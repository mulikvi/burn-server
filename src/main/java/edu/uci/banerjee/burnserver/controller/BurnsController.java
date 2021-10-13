package edu.uci.banerjee.burnserver.controller;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;
import edu.uci.banerjee.burnserver.model.Fires;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
public class BurnsController {
    @Autowired
    FiresRepo repo;

    @PostMapping("/load")
    public String loadBurnData(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        CsvParserSettings csvSettings = new CsvParserSettings();
        csvSettings.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(csvSettings);
        List<Record> allRecords = parser.parseAllRecords(inputStream);
        List<Fires> burns = new ArrayList<>();

        allRecords.forEach(record -> {
            Fires fire = new Fires();
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
            fire.setBurntype(record.getString("burn_type"));
            fire.setCounty(record.getString("county"));
            fire.setSource(record.getString("source"));
            burns.add(fire);
        });

       repo.saveAll(burns);
        return "load success";
    }

}
