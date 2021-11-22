package edu.uci.banerjee.burnserver.services;

import com.univocity.parsers.common.record.Record;
import edu.uci.banerjee.burnserver.model.Fire;
import edu.uci.banerjee.burnserver.model.FiresRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.BooleanSupplier;

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

    final var fire = new Fire();
    fire.setName(fireRecord.getString("name"));
    fire.setAcres(Double.parseDouble(fireRecord.getString("acres")));
    fire.setLatitude(Double.parseDouble(fireRecord.getString("latitude")));
    fire.setLongitude(Double.parseDouble(fireRecord.getString("longitude")));
    fire.setBurnType(fireRecord.getString("burn_type"));
    fire.setTreatmentType(fireRecord.getString("treatment_type"));
    fire.setCountyUnitId(fireRecord.getString("county_unit_ID"));
    fire.setCounty(fireRecord.getString("county"));
    fire.setSource(fireRecord.getString("source"));
    fire.setEscaped(Boolean.parseBoolean(fireRecord.getString("escaped")));
    fire.setOwner(
        landOwnershipService.getOwnershipFromCoordinate(fire.getLatitude(), fire.getLongitude()));

    try {
      final var fireDate = Calendar.getInstance();
      fireDate.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(fireRecord.getString("date")));
      fire.setYear(fireDate.get(Calendar.YEAR));
      fire.setMonth(fireDate.get(Calendar.MONTH));
      fire.setDay(fireDate.get(Calendar.DAY_OF_MONTH));
    } catch (ParseException e) {
      log.warn("Record : " + fireRecord.toString());
      log.warn("Date is Invalid. " + e.getMessage());
    }

    return fire;
  }
}
