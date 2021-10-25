package edu.uci.banerjee.burnserver.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Date;
import java.util.List;

@CrossOrigin(origins = "*")
@RepositoryRestResource(collectionResourceRel = "fires",path="fires")
public interface FiresRepo extends JpaRepository<Fires, Integer> {

    List<Fires> findBySource(String source);

    List<Fires> findByCounty(String county);

    List<Fires> findByBurnType(String burnType);

    List<Fires> findByYear(int year);

    List<Fires> findByYearIsBetween(int fromYear, int toYear);

    List<Fires> findByAcres(double acres);

    List<Fires> findByAcresIsBetween(double min, double max);

    List<Fires> findByDateIsBetween(Date fromDate, Date toDate);

}
