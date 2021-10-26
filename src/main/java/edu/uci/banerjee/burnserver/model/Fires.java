package edu.uci.banerjee.burnserver.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "fires", indexes={@Index(columnList="source",name="sourceindex"),
        @Index(columnList = "county", name="countyindex"),
        @Index(columnList = "year", name ="yearindex")})
public class Fires {
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

}
