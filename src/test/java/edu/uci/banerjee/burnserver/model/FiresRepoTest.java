package edu.uci.banerjee.burnserver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;

import java.text.SimpleDateFormat;
import java.util.List;


@DataJpaTest
public class FiresRepoTest {
    @Autowired
    private FiresRepo repo;

    Fires fire;
    @BeforeEach
    void initialize()throws Exception{
        fire = new Fires(2009,new SimpleDateFormat("dd/MM/yyyy").parse("5/31/2009"),"Camp",0.05,38.90,-121.06,"Hand Pile"
                ,"Modesto","CALFIRE");
        repo.save(fire);
    }

    public FiresRepoTest(){
    }

    @Test
    public void save() {

        Assertions.assertThat(fire.getId()).isGreaterThan(0);
    }

    @Test
    void findAll(){

        Fires returnedFire = repo.findById(1).get();
        Assertions.assertThat(returnedFire.getId()).isEqualTo(1);

        List<Fires> firesList = repo.findAll();
        Assertions.assertThat(firesList).isNotNull();
    }

    @Test
    void findBySource() {
        List<Fires> firesList = repo.findBySource("CALFIRE");
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByCounty() {
        List<Fires> firesList = repo.findByCounty("Modesto");
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByBurntype() {
        List<Fires> firesList = repo.findByBurnType("Hand Pile");
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByYear() {
        List<Fires> firesList = repo.findByYear(2009);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByAcres() {
        List<Fires> firesList = repo.findByAcres(0.05);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }
}