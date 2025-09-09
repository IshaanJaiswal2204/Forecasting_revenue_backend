package com.revenueforecast.model;

import jakarta.persistence.*;

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

    // Constructors
    public BFD() {}

    public BFD(Integer projectId, String projectDescription, Double jan, Double feb, Double mar, Double apr,
               Double may, Double jun, Double jul, Double aug, Double sep, Double oct, Double nov, Double dec) {
        this.projectId = projectId;
        this.projectDescription = projectDescription;
        this.jan = jan;
        this.feb = feb;
        this.mar = mar;
        this.apr = apr;
        this.may = may;
        this.jun = jun;
        this.jul = jul;
        this.aug = aug;
        this.sep = sep;
        this.oct = oct;
        this.nov = nov;
        this.dec = dec;
    }

    // Getters and Setters
    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }

    public Double getJan() { return jan; }
    public void setJan(Double jan) { this.jan = jan; }

    public Double getFeb() { return feb; }
    public void setFeb(Double feb) { this.feb = feb; }

    public Double getMar() { return mar; }
    public void setMar(Double mar) { this.mar = mar; }

    public Double getApr() { return apr; }
    public void setApr(Double apr) { this.apr = apr; }

    public Double getMay() { return may; }
    public void setMay(Double may) { this.may = may; }

    public Double getJun() { return jun; }
    public void setJun(Double jun) { this.jun = jun; }

    public Double getJul() { return jul; }
    public void setJul(Double jul) { this.jul = jul; }

    public Double getAug() { return aug; }
    public void setAug(Double aug) { this.aug = aug; }

    public Double getSep() { return sep; }
    public void setSep(Double sep) { this.sep = sep; }

    public Double getOct() { return oct; }
    public void setOct(Double oct) { this.oct = oct; }

    public Double getNov() { return nov; }
    public void setNov(Double nov) { this.nov = nov; }

    public Double getDec() { return dec; }
    public void setDec(Double dec) { this.dec = dec; }
}
