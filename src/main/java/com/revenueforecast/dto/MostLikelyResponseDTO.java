package com.revenueforecast.dto;

import java.time.LocalDate;

public class MostLikelyResponseDTO {

    private Integer associateId;
    private String name;
    private String serviceLine;
    private Integer projectId;
    private String projectName;
    private LocalDate currentStart;
    private LocalDate currentEnd;
    private String billability;
    private String city;
    private Double rate;
    private Double hours;
    private Double monthHours;
    private Double monthRevenue;
    private Double confidencePercent;
    private Double previousRevenue;
    private Double variance;

    // Getters and Setters

    public Integer getAssociateId() {
        return associateId;
    }

    public void setAssociateId(Integer associateId) {
        this.associateId = associateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceLine() {
        return serviceLine;
    }

    public void setServiceLine(String serviceLine) {
        this.serviceLine = serviceLine;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public LocalDate getCurrentStart() {
        return currentStart;
    }

    public void setCurrentStart(LocalDate currentStart) {
        this.currentStart = currentStart;
    }

    public LocalDate getCurrentEnd() {
        return currentEnd;
    }

    public void setCurrentEnd(LocalDate currentEnd) {
        this.currentEnd = currentEnd;
    }

    public String getBillability() {
        return billability;
    }

    public void setBillability(String billability) {
        this.billability = billability;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getHours() {
        return hours;
    }

    public void setHours(Double hours) {
        this.hours = hours;
    }

    public Double getMonthHours() {
        return monthHours;
    }

    public void setMonthHours(Double monthHours) {
        this.monthHours = monthHours;
    }

    public Double getMonthRevenue() {
        return monthRevenue;
    }

    public void setMonthRevenue(Double monthRevenue) {
        this.monthRevenue = monthRevenue;
    }

    public Double getConfidencePercent() {
        return confidencePercent;
    }

    public void setConfidencePercent(Double confidencePercent) {
        this.confidencePercent = confidencePercent;
    }

    public Double getPreviousRevenue() {
        return previousRevenue;
    }

    public void setPreviousRevenue(Double previousRevenue) {
        this.previousRevenue = previousRevenue;
    }

    public Double getVariance() {
        return variance;
    }

    public void setVariance(Double variance) {
        this.variance = variance;
    }
}
