package com.revenueforecast.model;

import jakarta.persistence.*;

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

    // Constructors
    public CognizantHoliday() {}

    public CognizantHoliday(String location, Integer jan, Integer feb, Integer mar, Integer apr, Integer may,
                            Integer jun, Integer jul, Integer aug, Integer sep, Integer oct, Integer nov,
                            Integer dec, Integer total) {
        this.location = location;
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
        this.total = total;
    }

    // Getters and Setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Integer getJan() { return jan; }
    public void setJan(Integer jan) { this.jan = jan; }

    public Integer getFeb() { return feb; }
    public void setFeb(Integer feb) { this.feb = feb; }

    public Integer getMar() { return mar; }
    public void setMar(Integer mar) { this.mar = mar; }

    public Integer getApr() { return apr; }
    public void setApr(Integer apr) { this.apr = apr; }

    public Integer getMay() { return may; }
    public void setMay(Integer may) { this.may = may; }

    public Integer getJun() { return jun; }
    public void setJun(Integer jun) { this.jun = jun; }

    public Integer getJul() { return jul; }
    public void setJul(Integer jul) { this.jul = jul; }

    public Integer getAug() { return aug; }
    public void setAug(Integer aug) { this.aug = aug; }

    public Integer getSep() { return sep; }
    public void setSep(Integer sep) { this.sep = sep; }

    public Integer getOct() { return oct; }
    public void setOct(Integer oct) { this.oct = oct; }

    public Integer getNov() { return nov; }
    public void setNov(Integer nov) { this.nov = nov; }

    public Integer getDec() { return dec; }
    public void setDec(Integer dec) { this.dec = dec; }

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
}
