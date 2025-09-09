package com.revenueforecast.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.revenueforecast.service.ExcelUploadService;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Autowired
    private ExcelUploadService excelUploadService;

    @PostMapping
    public ResponseEntity<String> uploadFiles(
            @RequestParam(value = "baseline", required = false) MultipartFile baselineFile,
            @RequestParam(value = "mostLikely", required = false) MultipartFile mostLikelyFile,
            @RequestParam(value = "bfd", required = false) MultipartFile bfdFile,
            @RequestParam(value = "cognizantHoliday", required = false) MultipartFile cognizantHolidayFile,
            @RequestParam(value = "associateHoliday", required = false) MultipartFile associateHolidayFile) {

        StringBuilder response = new StringBuilder();

        try {
            if (baselineFile != null && !baselineFile.isEmpty()) {
                excelUploadService.uploadBaseline(baselineFile);
                response.append("✅ Baseline uploaded successfully.\n");
            }

            if (mostLikelyFile != null && !mostLikelyFile.isEmpty()) {
                excelUploadService.uploadMostLikely(mostLikelyFile);
                response.append("✅ MostLikely uploaded successfully.\n");
            }

            if (bfdFile != null && !bfdFile.isEmpty()) {
                excelUploadService.uploadBFD(bfdFile);
                response.append("✅ BFD uploaded successfully.\n");
            }

            if (cognizantHolidayFile != null && !cognizantHolidayFile.isEmpty()) {
                excelUploadService.uploadCognizantHoliday(cognizantHolidayFile);
                response.append("✅ CognizantHoliday uploaded successfully.\n");
            }

            if (associateHolidayFile != null && !associateHolidayFile.isEmpty()) {
                excelUploadService.uploadAssociateHoliday(associateHolidayFile);
                response.append("✅ AssociateHoliday uploaded successfully.\n");
            }

            if (response.length() == 0) {
                return ResponseEntity.badRequest().body("⚠️ No files provided for upload.");
            }

            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("❌ Upload failed: " + e.getMessage());
        }
    }
}
