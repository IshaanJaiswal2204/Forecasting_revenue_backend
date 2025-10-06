package com.revenueforecast.dto;

import lombok.Data;

@Data
public class BFDResponseDTO {
    private Integer projectId;
    private String projectDescription;
    private Double jan;
    private Double feb;
    private Double mar;
    private Double apr;
    private Double may;
    private Double jun;
    private Double jul;
    private Double aug;
    private Double sep;
    private Double oct;
    private Double nov;
    private Double dec;
}
