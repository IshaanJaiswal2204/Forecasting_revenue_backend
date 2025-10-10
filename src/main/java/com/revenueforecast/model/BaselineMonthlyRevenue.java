package com.revenueforecast.model;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "BaselineMonthlyRevenue", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"associateId", "year", "month"})
})
public class BaselineMonthlyRevenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer associateId;
    private String associateName;
    private Integer year;
    private Integer month;

    private Double monthHours;
    private Double monthRevenue;
    private Double previousRevenue;
    private Double variance;

    private Double rate;
    private String billability;
    private LocalDate startDate;
    private LocalDate endDate;

    private LocalDate calculatedOn;

    @Version
    private Integer version; // For optimistic locking
}
