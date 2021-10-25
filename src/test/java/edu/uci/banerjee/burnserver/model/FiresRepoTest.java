package edu.uci.banerjee.burnserver.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.assertj.core.api.Assertions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@DataJpaTest
public class FiresRepoTest {
    @Autowired
    private FiresRepo repo;

    Fires fire;
    @BeforeEach
    void initialize()throws Exception{
        fire = new Fires(1,0.05,"Hand Pile","Modesto",new SimpleDateFormat("dd/MM/yyyy").parse("12/10/2010"),38.90,-121.05,"Camp","CALFIRE",2010);
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

        List<Fires> firesList = repo.findAll();
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
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
        List<Fires> firesList = repo.findByYear(2010);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByYearIsBetween() {
        List<Fires> firesList = repo.findByYearIsBetween(2000,2020);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByAcres() {
        List<Fires> firesList = repo.findByAcres(0.05);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByAcresIsBetween(){
        List<Fires> firesList =  repo.findByAcresIsBetween(0.0,20.0);
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }

    @Test
    void findByDateIsBetween() throws ParseException {
        List<Fires> firesList =  repo.findByDateIsBetween(new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2000"),new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2011"));
        Assertions.assertThat(firesList).isNotNull();
        Assertions.assertThat(firesList.size()).isGreaterThan(0);
    }
}