package edu.uci.banerjee.burnserver.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RepositoryRestResource(collectionResourceRel = "fires",path="fires")
public interface FiresRepo extends JpaRepository<Fires, Integer> {
    List<Fires> findBySource(String source);

    List<Fires> findByCounty(String county);

    List<Fires> findByBurntype(String burntype);

    List<Fires> findByYear(int year);

    List<Fires> findByAcres(double acres);



}
