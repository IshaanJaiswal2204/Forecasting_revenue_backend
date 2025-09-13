package com.revenueforecast.controller;

import com.revenueforecast.service.ExcelUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private ExcelUploadService excelUploadService;

    @PostMapping
    public ResponseEntity<String> uploadFiles(
            @RequestParam(value = "baseline", required = false) MultipartFile baselineFile,
            @RequestParam(value = "mostLikely", required = false) MultipartFile mostLikelyFile,
            @RequestParam(value = "bfd", required = false) MultipartFile bfdFile,
            @RequestParam(value = "cognizantHoliday", required = false) MultipartFile cognizantHolidayFile,
            @RequestParam(value = "associateHoliday", required = false) MultipartFile associateHolidayFile) {

        logger.info("Received upload request");

        StringBuilder response = new StringBuilder();

        try {
            if (baselineFile != null && !baselineFile.isEmpty()) {
                logger.info("Processing baseline file: {}", baselineFile.getOriginalFilename());
                excelUploadService.uploadBaseline(baselineFile);
                response.append("✅ Baseline uploaded successfully.\n");
            }

            if (mostLikelyFile != null && !mostLikelyFile.isEmpty()) {
                logger.info("Processing mostLikely file: {}", mostLikelyFile.getOriginalFilename());
                excelUploadService.uploadMostLikely(mostLikelyFile);
                response.append("✅ MostLikely uploaded successfully.\n");
            }

            if (bfdFile != null && !bfdFile.isEmpty()) {
                logger.info("Processing BFD file: {}", bfdFile.getOriginalFilename());
                excelUploadService.uploadBFD(bfdFile);
                response.append("✅ BFD uploaded successfully.\n");
            }

            if (cognizantHolidayFile != null && !cognizantHolidayFile.isEmpty()) {
                logger.info("Processing CognizantHoliday file: {}", cognizantHolidayFile.getOriginalFilename());
                excelUploadService.uploadCognizantHoliday(cognizantHolidayFile);
                response.append("✅ CognizantHoliday uploaded successfully.\n");
            }

            if (associateHolidayFile != null && !associateHolidayFile.isEmpty()) {
                logger.info("Processing AssociateHoliday file: {}", associateHolidayFile.getOriginalFilename());
                excelUploadService.uploadAssociateHoliday(associateHolidayFile);
                response.append("✅ AssociateHoliday uploaded successfully.\n");
            }

            if (response.length() == 0) {
                logger.warn("No files provided for upload");
                return ResponseEntity.badRequest().body("⚠️ No files provided for upload.");
            }

            logger.info("Upload completed successfully");
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            logger.error("Upload failed", e);
            return ResponseEntity.status(500).body("❌ Upload failed: " + e.getMessage());
        }
    }
}
