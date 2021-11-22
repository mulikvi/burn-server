package edu.uci.banerjee.burnserver.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class FiresRepoTest {
  @Autowired private FiresRepo repo;

  private Fire fire;

  public FiresRepoTest() {}

  @BeforeEach
  void initialize() throws Exception {
    fire =
        new Fire(
            0.05,
            "Hand Pile",
            null,
            "MEU",
            "Modesto",
            38.90,
            -121.05,
            "Camp",
            "CALFIRE",
            2010,
            12,
            22,
            "Private",
            10.25,
            false);
    repo.save(fire);
  }

  @Test
  public void save() {
    Assertions.assertThat(fire.getId()).isGreaterThan(0);
  }

  @Test
  void findAll() {
    List<Fire> firesList = repo.findAll();
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findBySource() {
    List<Fire> firesList = repo.findBySource("CALFIRE");
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByCounty() {
    List<Fire> firesList = repo.findByCounty("Modesto");
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByBurnType() {
    List<Fire> firesList = repo.findByBurnType("Hand Pile");
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByYear() {
    List<Fire> firesList = repo.findByYear(2010);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByYearIsBetween() {
    List<Fire> firesList = repo.findByYearIsBetween(2000, 2020);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByAcres() {
    List<Fire> firesList = repo.findByAcres(0.05);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByAcresIsBetween() {
    List<Fire> firesList = repo.findByAcresIsBetween(0.0, 20.0);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
  }

  @Test
  void findByOwner() {
    List<Fire> firesList = repo.findByOwner("Private");
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isEqualTo(1);
  }

  @Test
  void findBySeverityBetween() {
    List<Fire> firesList = repo.findBySeverityBetween(0.0, 30.11);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
    Assertions.assertThat(firesList.size()).isEqualTo(1);
  }

  @Test
  void findByEscapedFalse() {
    List<Fire> firesList = repo.findByEscapedFalse();
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
    Assertions.assertThat(firesList.size()).isEqualTo(1);
  }

  @Test
  void findByEscapedTrue() {
    List<Fire> firesList = repo.findByEscapedTrue();
    Assertions.assertThat(firesList.size()).isEqualTo(0);
  }

  @Test
  void findByAllParams() {
    List<Fire> firesList =
        repo.findByAllParams(null, null, null, null, null, null, null, null, null, null, null, null, null);
    Assertions.assertThat(firesList).isNotNull();
    Assertions.assertThat(firesList.size()).isGreaterThan(0);
    Assertions.assertThat(firesList.size()).isEqualTo(1);
  }
}
