package edu.uci.banerjee.burnserver.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Fires {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private int id;

    @Column(name = "year")
    private int year;

    @Column(name = "date")
    private Date date;

    @Column(name = "name")
    private String name;

    @Column(name = "acres")
    private double acres;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "burnType")
    private String burnType;

    @Column(name = "county")
    private String county;

    @Column(name = "source")
    private String source;

    public Fires(int year, Date date, String name, double acres, double latitude, double longitude, String burnType, String county, String source) {
        this.year = year;
        this.date = date;
        this.name = name;
        this.acres = acres;
        this.latitude = latitude;
        this.longitude = longitude;
        this.burnType = burnType;
        this.county = county;
        this.source = source;
    }
}
