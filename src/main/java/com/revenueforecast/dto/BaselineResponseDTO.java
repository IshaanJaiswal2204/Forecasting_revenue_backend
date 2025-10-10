package com.revenueforecast.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BaselineResponseDTO {

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

    private Integer year;              // ✅ Added
    private Integer month;             // ✅ Added

    private Double monthHours;
    private Double monthRevenue;
    private Double previousRevenue;
    private Double variance;

    private LocalDate calculatedOn;    // ✅ Optional: only if you want to return this in response
}
