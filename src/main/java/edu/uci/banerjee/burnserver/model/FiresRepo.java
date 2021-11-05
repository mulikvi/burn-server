package edu.uci.banerjee.burnserver.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "fires", path = "fires")
public interface FiresRepo extends JpaRepository<Fire, Integer> {
  List<Fire> findBySource(String source);

  List<Fire> findByCounty(String county);

  List<Fire> findByBurnType(String burnType);

  List<Fire> findByYear(int year);

  List<Fire> findByYearIsBetween(int fromYear, int toYear);

  List<Fire> findByAcres(double acres);

  List<Fire> findByAcresIsBetween(double min, double max);

  List<Fire> findByDateIsBetween(Date fromDate, Date toDate);

  List<Fire> findByOwner(String owner);

  List<Fire> findByIntensityBetween(double min, double max);
}
