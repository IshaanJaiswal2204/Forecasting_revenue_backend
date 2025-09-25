package com.revenueforecast.service;

import com.revenueforecast.model.*;
import com.revenueforecast.repository.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelUploadService {

    private static final Logger logger = LoggerFactory.getLogger(ExcelUploadService.class);

    @Autowired private BaselineRepository baselineRepo;
    @Autowired private BFDRepository bfdRepo;
    @Autowired private CognizantHolidayRepository cognizantHolidayRepo;
    @Autowired private AssociateHolidayRepository associateHolidayRepo;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    public void uploadBaseline(MultipartFile file) throws Exception {
        logger.info("Uploading Baseline data from file: {}", file.getOriginalFilename());
        List<Baseline> records = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                Baseline b = new Baseline();
                b.setAssociateId(getInteger(row, 0));
                b.setAssociateName(getCell(row, 1));
                b.setProjectId(getInteger(row, 2));
                b.setProjectDescription(getCell(row, 3));
                b.setProjectBillability(getCell(row, 4));
                b.setProjectManagerId(getInteger(row, 5));
                b.setProjectManagerName(getCell(row, 6));
                b.setGrade(getCell(row, 7));
                b.setSl(getCell(row, 8));
                b.setBillabilityStatus(getCell(row, 9));
                b.setCountry(getCell(row, 10));
                b.setCity(getCell(row, 11));
                b.setPercentAllocation(getDouble(row, 12));
                b.setBillRate(getDouble(row, 13));
                b.setCurrentStartDate(getDate(row, 14));
                b.setCurrentEndDate(getDate(row, 15));

                // Derive MostLikely dates and confidence
                LocalDate ced = b.getCurrentEndDate();
                if (ced != null && !(ced.getMonthValue() == 12 && ced.getDayOfMonth() == 31)) {
                    b.setMostLikelyStartDate(ced.plusDays(1));
                    b.setMostLikelyEndDate(LocalDate.of(ced.getYear(), 12, 31));
                } else {
                    b.setMostLikelyStartDate(null);
                    b.setMostLikelyEndDate(null);
                }
                b.setConfidentPercentage(95.0);

                records.add(b);
            }
        }
        baselineRepo.saveAll(records);
        logger.info("Saved {} Baseline records", records.size());
    }

    public void uploadBFD(MultipartFile file) throws Exception {
        logger.info("Uploading BFD data from file: {}", file.getOriginalFilename());
        List<BFD> records = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                BFD bfd = new BFD();
                bfd.setProjectId(getInteger(row, 0));
                bfd.setProjectDescription(getCell(row, 1));
                bfd.setJan(getDouble(row, 2));
                bfd.setFeb(getDouble(row, 3));
                bfd.setMar(getDouble(row, 4));
                bfd.setApr(getDouble(row, 5));
                bfd.setMay(getDouble(row, 6));
                bfd.setJun(getDouble(row, 7));
                bfd.setJul(getDouble(row, 8));
                bfd.setAug(getDouble(row, 9));
                bfd.setSep(getDouble(row, 10));
                bfd.setOct(getDouble(row, 11));
                bfd.setNov(getDouble(row, 12));
                bfd.setDec(getDouble(row, 13));
                records.add(bfd);
            }
        }
        bfdRepo.saveAll(records);
        logger.info("Saved {} BFD records", records.size());
    }

    public void uploadCognizantHoliday(MultipartFile file) throws Exception {
        logger.info("Uploading CognizantHoliday data from file: {}", file.getOriginalFilename());
        List<CognizantHoliday> records = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                CognizantHoliday ch = new CognizantHoliday();
                ch.setLocation(getCell(row, 0));
                ch.setJan(getInteger(row, 1));
                ch.setFeb(getInteger(row, 2));
                ch.setMar(getInteger(row, 3));
                ch.setApr(getInteger(row, 4));
                ch.setMay(getInteger(row, 5));
                ch.setJun(getInteger(row, 6));
                ch.setJul(getInteger(row, 7));
                ch.setAug(getInteger(row, 8));
                ch.setSep(getInteger(row, 9));
                ch.setOct(getInteger(row, 10));
                ch.setNov(getInteger(row, 11));
                ch.setDec(getInteger(row, 12));
                ch.setTotal(getInteger(row, 13));
                records.add(ch);
            }
        }
        cognizantHolidayRepo.saveAll(records);
        logger.info("Saved {} CognizantHoliday records", records.size());
    }

    public void uploadAssociateHoliday(MultipartFile file) throws Exception {
        logger.info("Uploading AssociateHoliday data from file: {}", file.getOriginalFilename());
        List<AssociateHoliday> records = new ArrayList<>();
        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                AssociateHoliday ah = new AssociateHoliday();
                ah.setAssociateId(getInteger(row, 0));
                ah.setAssociateName(getCell(row, 1));
                ah.setJan(getInteger(row, 2));
                ah.setFeb(getInteger(row, 3));
                ah.setMar(getInteger(row, 4));
                ah.setApr(getInteger(row, 5));
                ah.setMay(getInteger(row, 6));
                ah.setJun(getInteger(row, 7));
                ah.setJul(getInteger(row, 8));
                ah.setAug(getInteger(row, 9));
                ah.setSep(getInteger(row, 10));
                ah.setOct(getInteger(row, 11));
                ah.setNov(getInteger(row, 12));
                ah.setDec(getInteger(row, 13));
                records.add(ah);
            }
        }
        associateHolidayRepo.saveAll(records);
        logger.info("Saved {} AssociateHoliday records", records.size());
    }

    private Integer getInteger(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return 0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return (int) Math.round(cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Integer.parseInt(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse integer from cell at index {}: '{}'", index, cell.getStringCellValue());
                return 0;
            }
        }
        return 0;
    }

    private String getCell(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell != null ? cell.toString().trim() : "";
    }

    private Double getDouble(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return 0.0;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getNumericCellValue();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return Double.parseDouble(cell.getStringCellValue().trim());
            } catch (NumberFormatException e) {
                logger.warn("Failed to parse double from cell at index {}: '{}'", index, cell.getStringCellValue());
                return 0.0;
            }
        }
        return 0.0;
    }

    private LocalDate getDate(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else if (cell.getCellType() == CellType.STRING) {
            try {
                return LocalDate.parse(cell.getStringCellValue().trim(), dateFormatter);
            } catch (Exception e) {
                logger.warn("Failed to parse date from cell at index {}: '{}'", index, cell.getStringCellValue());
                return null;
            }
        }
        return null;
    }
}
