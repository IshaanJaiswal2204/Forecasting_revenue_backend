package com.revenueforecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

}
