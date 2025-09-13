package com.revenueforecast.controller;

import com.revenueforecast.dto.BaselineResponseDTO;
import com.revenueforecast.service.BaselineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/baseline")
public class BaselineController {

    private static final Logger logger = LoggerFactory.getLogger(BaselineController.class);

    @Autowired
    private BaselineService baselineService;

    @GetMapping("/view")
    public ResponseEntity<List<BaselineResponseDTO>> getBaselineByMonth(
            @RequestParam int month,
            @RequestParam int year) {
        logger.info("Received request for baseline view: month={}, year={}", month, year);

        List<BaselineResponseDTO> response = baselineService.getBaselineByMonth(month, year);
        logger.info("Returning {} baseline records", response.size());

        return ResponseEntity.ok(response);
    }
}
