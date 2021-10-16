package edu.uci.banerjee.burnserver.model;

import javax.persistence.*;
import java.util.Date;


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
    @Column(name="burntype")
    private String burntype;
    @Column(name = "county")
    private String county;
    @Column(name = "source")
    private String source;

    public Fires() {

    }

    public Fires(int year, Date date, String name, double acres, double latitude, double longitude, String burn_type, String county, String source) {
        this.year = year;
        this.date = date;
        this.name = name;
        this.acres = acres;
        this.latitude = latitude;
        this.longitude = longitude;
        this.burntype = burntype;
        this.county = county;
        this.source = source;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAcres() {
        return acres;
    }

    public void setAcres(double acres) {
        this.acres = acres;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBurntype() {
        return burntype;
    }

    public void setBurntype(String burntype) {
        this.burntype = burntype;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "Fires{" +
                "id=" + id +
                ", year=" + year +
                ", date=" + date +
                ", name='" + name + '\'' +
                ", acres=" + acres +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", burntype='" + burntype + '\'' +
                ", county='" + county + '\'' +
                ", source='" + source + '\'' +
                '}';
    }
}
