package com.revenueforecast.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "MostLikely")
public class MostLikely {

    @Id
    @Column(name = "`associate_id`")
    private Integer associateId;

    @Column(name = "`associate_name`")
    private String associateName;

    @Column(name = "`project_id`")
    private Integer projectId;

    @Column(name = "`project_description`")
    private String projectDescription;

    @Column(name = "`project_billability`")
    private String projectBillability;

    @Column(name = "`project_manager_name`")
    private String projectManagerName;

    @Column(name = "`grade`")
    private String grade;

    @Column(name = "`sl`")
    private String sl;

    @Column(name = "`billability_status`")
    private String billabilityStatus;

    @Column(name = "`country`")
    private String country;

    @Column(name = "`city`")
    private String city;

    @Column(name = "`percent_allocation`")
    private Double percentAllocation;

    @Column(name = "`bill_rate`")
    private Double billRate;

    @Column(name = "`current_start_date`")
    private LocalDate currentStartDate;

    @Column(name = "`current_end_date`")
    private LocalDate currentEndDate;

    // Constructors
    public MostLikely() {}

    public MostLikely(Integer associateId, String associateName, Integer projectId, String projectDescription,
                      String projectBillability, String projectManagerName, String grade, String sl,
                      String billabilityStatus, String country, String city, Double percentAllocation,
                      Double billRate, LocalDate currentStartDate, LocalDate currentEndDate) {
        this.associateId = associateId;
        this.associateName = associateName;
        this.projectId = projectId;
        this.projectDescription = projectDescription;
        this.projectBillability = projectBillability;
        this.projectManagerName = projectManagerName;
        this.grade = grade;
        this.sl = sl;
        this.billabilityStatus = billabilityStatus;
        this.country = country;
        this.city = city;
        this.percentAllocation = percentAllocation;
        this.billRate = billRate;
        this.currentStartDate = currentStartDate;
        this.currentEndDate = currentEndDate;
    }

    // Getters and Setters
    public Integer getAssociateId() { return associateId; }
    public void setAssociateId(Integer associateId) { this.associateId = associateId; }

    public String getAssociateName() { return associateName; }
    public void setAssociateName(String associateName) { this.associateName = associateName; }

    public Integer getProjectId() { return projectId; }
    public void setProjectId(Integer projectId) { this.projectId = projectId; }

    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }

    public String getProjectBillability() { return projectBillability; }
    public void setProjectBillability(String projectBillability) { this.projectBillability = projectBillability; }

    public String getProjectManagerName() { return projectManagerName; }
    public void setProjectManagerName(String projectManagerName) { this.projectManagerName = projectManagerName; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }

    public String getSl() { return sl; }
    public void setSl(String sl) { this.sl = sl; }

    public String getBillabilityStatus() { return billabilityStatus; }
    public void setBillabilityStatus(String billabilityStatus) { this.billabilityStatus = billabilityStatus; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public Double getPercentAllocation() { return percentAllocation; }
    public void setPercentAllocation(Double percentAllocation) { this.percentAllocation = percentAllocation; }

    public Double getBillRate() { return billRate; }
    public void setBillRate(Double billRate) { this.billRate = billRate; }

    public LocalDate getCurrentStartDate() { return currentStartDate; }
    public void setCurrentStartDate(LocalDate currentStartDate) { this.currentStartDate = currentStartDate; }

    public LocalDate getCurrentEndDate() { return currentEndDate; }
    public void setCurrentEndDate(LocalDate currentEndDate) { this.currentEndDate = currentEndDate; }
}
