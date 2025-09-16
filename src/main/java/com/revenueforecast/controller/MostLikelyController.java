package com.revenueforecast.controller;

import com.revenueforecast.dto.MostLikelyResponseDTO;
import com.revenueforecast.service.MostLikelyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mostlikely")
public class MostLikelyController {

    private static final Logger logger = LoggerFactory.getLogger(MostLikelyController.class);

    @Autowired
    private MostLikelyService mostLikelyService;

    @GetMapping("/view")
    public ResponseEntity<Page<MostLikelyResponseDTO>> getMostLikelyByMonth(
            @RequestParam int month,
            @RequestParam int year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        logger.info("Received request for MostLikely view: month={}, year={}, page={}, size={}", month, year, page, size);

        Page<MostLikelyResponseDTO> response = mostLikelyService.getMostLikelyByMonth(month, year, page, size);

        logger.info("Returning {} MostLikely records on page {}", response.getNumberOfElements(), page);

        return ResponseEntity.ok(response);
    }
}
