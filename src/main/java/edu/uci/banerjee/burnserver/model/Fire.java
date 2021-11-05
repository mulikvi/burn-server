package edu.uci.banerjee.burnserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(
    name = "fires",
    indexes = {
      @Index(columnList = "source", name = "sourceIndex"),
      @Index(columnList = "county", name = "countyIndex"),
      @Index(columnList = "year", name = "yearIndex")
    })
public class Fire {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, unique = true)
  private int id;

  @Column(name = "acres")
  private double acres;

  @Column(name = "burnType")
  private String burnType;

  @Column(name = "county")
  private String county;

  @Column(name = "date")
  private Date date;

  @Column(name = "latitude")
  private double latitude;

  @Column(name = "longitude")
  private double longitude;

  @Column(name = "name")
  private String name;

  @Column(name = "source")
  private String source;

  @Column(name = "year")
  private int year;

  @Column(name = "owner")
  private String owner;

  @Column(name = "intensity")
  private double intensity;

  public Fire(
      double acres,
      String burnType,
      String county,
      Date date,
      double latitude,
      double longitude,
      String name,
      String source,
      int year,
      String owner,
      double intensity) {
    this.acres = acres;
    this.burnType = burnType;
    this.county = county;
    this.date = date;
    this.latitude = latitude;
    this.longitude = longitude;
    this.name = name;
    this.source = source;
    this.year = year;
    this.owner = owner;
    this.intensity = intensity;
  }
}
