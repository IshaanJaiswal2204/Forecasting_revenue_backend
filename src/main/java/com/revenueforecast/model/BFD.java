package com.revenueforecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BFD")
public class BFD {

    @Id
    @Column(name = "`project_id`")
    private Integer projectId;

    @Column(name = "`project_description`")
    private String projectDescription;

    @Column(name = "`jan`")
    private Double jan;

    @Column(name = "`feb`")
    private Double feb;

    @Column(name = "`mar`")
    private Double mar;

    @Column(name = "`apr`")
    private Double apr;

    @Column(name = "`may`")
    private Double may;

    @Column(name = "`jun`")
    private Double jun;

    @Column(name = "`jul`")
    private Double jul;

    @Column(name = "`aug`")
    private Double aug;

    @Column(name = "`sep`")
    private Double sep;

    @Column(name = "`oct`")
    private Double oct;

    @Column(name = "`nov`")
    private Double nov;

    @Column(name = "`dec`")
    private Double dec;
}
