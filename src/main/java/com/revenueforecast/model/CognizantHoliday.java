package com.revenueforecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "CognizantHoliday")
public class CognizantHoliday {

    @Id
    @Column(name = "`location`")
    private String location;

    @Column(name = "`jan`")
    private Integer jan;

    @Column(name = "`feb`")
    private Integer feb;

    @Column(name = "`mar`")
    private Integer mar;

    @Column(name = "`apr`")
    private Integer apr;

    @Column(name = "`may`")
    private Integer may;

    @Column(name = "`jun`")
    private Integer jun;

    @Column(name = "`jul`")
    private Integer jul;

    @Column(name = "`aug`")
    private Integer aug;

    @Column(name = "`sep`")
    private Integer sep;

    @Column(name = "`oct`")
    private Integer oct;

    @Column(name = "`nov`")
    private Integer nov;

    @Column(name = "`dec`")
    private Integer dec;

    @Column(name = "`total`")
    private Integer total;
}
