package com.revenueforecast.controller;

import com.revenueforecast.dto.BaselineResponseDTO;
import com.revenueforecast.service.BaselineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/baseline")
public class BaselineController {

    private static final Logger logger = LoggerFactory.getLogger(BaselineController.class);

    @Autowired
    private BaselineService baselineService;

    @GetMapping("/view")
    public ResponseEntity<Page<BaselineResponseDTO>> getBaselineByMonth(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Received request for baseline view: month={}, year={}, page={}, size={}", month, year, page, size);

        Page<BaselineResponseDTO> response = baselineService.getBaselineByMonth(month, year, page, size);

        logger.info("Returning {} baseline records on page {}", response.getNumberOfElements(), page);

        return ResponseEntity.ok(response);
    }
}
