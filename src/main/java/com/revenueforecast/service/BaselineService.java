package com.revenueforecast.service;
 
import com.revenueforecast.dao.BaselineRevenueJdbcDAO;
import com.revenueforecast.dao.MostLikelyRevenueJdbcDAO;
import com.revenueforecast.dto.BaselineResponseDTO;
import com.revenueforecast.dto.MostLikelyResponseDTO;
import com.revenueforecast.model.AssociateHoliday;
import com.revenueforecast.model.Baseline;
import com.revenueforecast.model.BaselineMonthlyRevenue;
import com.revenueforecast.model.CognizantHoliday;
import com.revenueforecast.repository.AssociateHolidayRepository;
import com.revenueforecast.repository.BaselineRepository;
import com.revenueforecast.repository.CognizantHolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
 
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.math.BigDecimal;
import java.math.RoundingMode;


 
@Service
public class BaselineService {
 
    private static final Logger logger = LoggerFactory.getLogger(BaselineService.class);
 
    @Autowired private BaselineRepository baselineRepository;
    @Autowired private AssociateHolidayRepository associateHolidayRepository;
    @Autowired private CognizantHolidayRepository cognizantHolidayRepository;
  


// 
//    public Page<BaselineResponseDTO> getBaselineByMonth(int month, int year, int page, int size) {
//        long startTime = System.currentTimeMillis();
//        logger.info("Calculating baseline for year={}, month={}, page={}, size={}", year, month, page, size);
// 
//        LocalDate firstDay = LocalDate.of(year, month, 1);
//        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
//        Pageable pageable = PageRequest.of(page, size);
// 
//        Page<Baseline> pageData = baselineRepository
//            .findByCurrentStartDateLessThanEqualAndCurrentEndDateGreaterThanEqual(lastDay, firstDay, pageable);
// 
//        List<BaselineResponseDTO> dtoList = pageData.getContent().stream()
//            .map(b -> {
//                BaselineResponseDTO dto = new BaselineResponseDTO();
//                dto.setAssociateId(b.getAssociateId());
//                dto.setName(b.getAssociateName());
//                dto.setServiceLine(b.getSl());
//                dto.setProjectId(b.getProjectId());
//                dto.setProjectName(b.getProjectDescription());
//                dto.setCurrentStart(b.getCurrentStartDate());
//                dto.setCurrentEnd(b.getCurrentEndDate());
//                dto.setBillability(b.getBillabilityStatus());
//                dto.setCity(b.getCity());
//                dto.setRate(b.getBillRate());
// 
//                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
//                dto.setHours(hours);
// 
//                LocalDate activeStart = b.getCurrentStartDate().isAfter(firstDay) ? b.getCurrentStartDate() : firstDay;
//                LocalDate activeEnd = b.getCurrentEndDate().isBefore(lastDay) ? b.getCurrentEndDate() : lastDay;
//                int workingDays = getWorkingDaysBetween(activeStart, activeEnd);
// 
//                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
//                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), activeStart, activeEnd);
// 
//                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);
// 
//                double monthHours = finalWorkingDays * hours;
//                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
//                double monthRevenue = rate * monthHours;
// 
//                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
//                int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
//                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
//                LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
//                LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());
//                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevStart, prevEnd);
// 
//                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
//                double previousMonthHours = finalPrevWorkingDays * hours;
//                double previousRevenue = rate * previousMonthHours;
// 
//                dto.setMonthHours(monthHours);
//                dto.setMonthRevenue(monthRevenue);
//                dto.setPreviousRevenue(previousRevenue);
//                dto.setVariance(monthRevenue - previousRevenue);
// 
//                return dto;
//            })
//            .collect(Collectors.toList());
// 
//        long endTime = System.currentTimeMillis();
//        logger.info("Baseline calculation completed in {} ms", endTime - startTime);
// 
//        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
//    }
    
    
//    
//    public Page<BaselineResponseDTO> getBaselineByMonth(int month, int year, int page, int size) {
//        long startTime = System.currentTimeMillis();
//        logger.info("Calculating baseline for year={}, month={}, page={}, size={}", year, month, page, size);
//
//        LocalDate firstDay = LocalDate.of(year, month, 1);
//        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
//        Pageable pageable = PageRequest.of(page, size);
//
//        Page<Baseline> pageData = baselineRepository
//            .findByCurrentStartDateLessThanEqualAndCurrentEndDateGreaterThanEqual(lastDay, firstDay, pageable);
//
//        List<BaselineResponseDTO> dtoList = pageData.getContent().stream()
//            .map(b -> {
//                BaselineResponseDTO dto = new BaselineResponseDTO();
//                dto.setAssociateId(b.getAssociateId());
//                dto.setName(b.getAssociateName());
//                dto.setServiceLine(b.getSl());
//                dto.setProjectId(b.getProjectId());
//                dto.setProjectName(b.getProjectDescription());
//                dto.setCurrentStart(b.getCurrentStartDate());
//                dto.setCurrentEnd(b.getCurrentEndDate());
//                dto.setBillability(b.getBillabilityStatus());
//                dto.setCity(b.getCity());
//                dto.setRate(b.getBillRate());
//
//                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
//                dto.setHours(hours);
//
//                LocalDate activeStart = b.getCurrentStartDate().isAfter(firstDay) ? b.getCurrentStartDate() : firstDay;
//                LocalDate activeEnd = b.getCurrentEndDate().isBefore(lastDay) ? b.getCurrentEndDate() : lastDay;
//                int workingDays = getWorkingDaysBetween(activeStart, activeEnd);
//
//                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
//                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), activeStart, activeEnd);
//
//                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);
//
//                double monthHours = finalWorkingDays * hours;
//                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
//                double monthRevenue = rate * monthHours;
//
//                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
//                int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
//                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
//                LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
//                LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());
//                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevStart, prevEnd);
//
//                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
//                double previousMonthHours = finalPrevWorkingDays * hours;
//                double previousRevenue = rate * previousMonthHours;
//
//                dto.setMonthHours(monthHours);
//                dto.setMonthRevenue(monthRevenue);
//                dto.setPreviousRevenue(previousRevenue);
//                dto.setVariance(monthRevenue - previousRevenue);
//
//                return dto;
//            })
//            .collect(Collectors.toList());
//
//        // 🧾 Store monthly revenue records
//        List<BaselineMonthlyRevenue> recordsToInsert = new ArrayList<>();
//
//        dtoList.forEach(dto -> {
//            Optional<BaselineMonthlyRevenue> existing = baselineMonthlyRevenueRepository
//                .findByAssociateIdAndYearAndMonth(dto.getAssociateId(), year, month);
//
//            if (existing.isEmpty()) {
//                BaselineMonthlyRevenue record = new BaselineMonthlyRevenue();
//                record.setAssociateId(dto.getAssociateId());
//                record.setAssociateName(dto.getName());
//                record.setYear(year);
//                record.setMonth(month);
//                record.setMonthHours(dto.getMonthHours());
//                record.setMonthRevenue(dto.getMonthRevenue());
//                record.setPreviousRevenue(dto.getPreviousRevenue());
//                record.setVariance(dto.getVariance());
//                record.setRate(dto.getRate());
//                record.setBillability(dto.getBillability());
//                record.setStartDate(dto.getCurrentStart());
//                record.setEndDate(dto.getCurrentEnd());
//                record.setCalculatedOn(LocalDate.now());
//                recordsToInsert.add(record);
//            } else {
//                BaselineMonthlyRevenue existingRecord = existing.get();
//                boolean changed = !Objects.equals(existingRecord.getMonthRevenue(), dto.getMonthRevenue())
//                    || !Objects.equals(existingRecord.getMonthHours(), dto.getMonthHours())
//                    || !Objects.equals(existingRecord.getRate(), dto.getRate())
//                    || !Objects.equals(existingRecord.getBillability(), dto.getBillability())
//                    || !Objects.equals(existingRecord.getStartDate(), dto.getCurrentStart())
//                    || !Objects.equals(existingRecord.getEndDate(), dto.getCurrentEnd());
//
//                if (changed) {
//                    existingRecord.setMonthHours(dto.getMonthHours());
//                    existingRecord.setMonthRevenue(dto.getMonthRevenue());
//                    existingRecord.setPreviousRevenue(dto.getPreviousRevenue());
//                    existingRecord.setVariance(dto.getVariance());
//                    existingRecord.setRate(dto.getRate());
//                    existingRecord.setBillability(dto.getBillability());
//                    existingRecord.setStartDate(dto.getCurrentStart());
//                    existingRecord.setEndDate(dto.getCurrentEnd());
//                    existingRecord.setCalculatedOn(LocalDate.now());
//                    baselineMonthlyRevenueRepository.save(existingRecord);
//                }
//            }
//        });
//
//        if (!recordsToInsert.isEmpty()) {
//            baselineMonthlyRevenueRepository.saveAll(recordsToInsert);
//        }
//
//        long endTime = System.currentTimeMillis();
//        logger.info("Baseline calculation completed in {} ms", endTime - startTime);
//
//        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
//    }
//
// 
    
    
    @Autowired
    private BaselineRevenueJdbcDAO baselineRevenueJdbcDAO;

    private double round(double value) {
        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public Page<BaselineResponseDTO> getBaselineByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating baseline for year={}, month={}, page={}, size={}", year, month, page, size);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        // 🔹 Prepare dynamic table
        String tableName = baselineRevenueJdbcDAO.getTableName(year, month);
        baselineRevenueJdbcDAO.createTableIfNotExists(tableName);

        List<BaselineResponseDTO> allDtos = new ArrayList<>();
        int chunkSize = 500;
        int offset = 0;

        while (true) {
            Pageable chunkPage = PageRequest.of(offset / chunkSize, chunkSize);
            Page<Baseline> chunk = baselineRepository
                .findByCurrentStartDateLessThanEqualAndCurrentEndDateGreaterThanEqual(lastDay, firstDay, chunkPage);

            if (chunk.isEmpty()) break;

            for (Baseline b : chunk.getContent()) {
                BaselineResponseDTO dto = new BaselineResponseDTO();
                dto.setAssociateId(b.getAssociateId());
                dto.setName(b.getAssociateName());
                dto.setServiceLine(b.getSl());
                dto.setProjectId(b.getProjectId());
                dto.setProjectName(b.getProjectDescription());
                dto.setCurrentStart(b.getCurrentStartDate());
                dto.setCurrentEnd(b.getCurrentEndDate());
                dto.setBillability(b.getBillabilityStatus());
                dto.setCity(b.getCity());
                dto.setRate(round(b.getBillRate() != null ? b.getBillRate() : 0));

                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
                dto.setHours(hours);
                dto.setYear(year);
                dto.setMonth(month);


                LocalDate activeStart = b.getCurrentStartDate().isAfter(firstDay) ? b.getCurrentStartDate() : firstDay;
                LocalDate activeEnd = b.getCurrentEndDate().isBefore(lastDay) ? b.getCurrentEndDate() : lastDay;
                int workingDays = getWorkingDaysBetween(activeStart, activeEnd);

                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), activeStart, activeEnd);

                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);
                double monthHours = finalWorkingDays * hours;
                double monthRevenue = dto.getRate() * monthHours;

                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
                LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
                LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());

                double previousRevenue = 0;
                if (!b.getCurrentStartDate().isAfter(prevEnd)) {
                    int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
                    int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
                    int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevStart, prevEnd);

                    int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
                    double previousMonthHours = finalPrevWorkingDays * hours;
                    previousRevenue = dto.getRate() * previousMonthHours;
                }

                dto.setMonthHours(round(monthHours));
                dto.setMonthRevenue(round(monthRevenue));
                dto.setPreviousRevenue(round(previousRevenue));
                dto.setVariance(round(monthRevenue - previousRevenue));
                dto.setCalculatedOn(LocalDate.now());


                allDtos.add(dto);
            }

            offset += chunkSize;
        }

        // 🔹 Store in dynamic table
        baselineRevenueJdbcDAO.batchInsertOrUpdate(tableName, allDtos,year,month);

        // Paginate final DTO list
        List<BaselineResponseDTO> pagedList = allDtos.stream()
            .skip((long) page * size)
            .limit(size)
            .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        logger.info("Baseline calculation completed in {} ms", endTime - startTime);

        return new PageImpl<>(pagedList, PageRequest.of(page, size), allDtos.size());
    }

//    
//    @Autowired
//    private MostLikelyRevenueJdbcDAO mostLikelyRevenueJdbcDAO;
//
//    public Page<MostLikelyResponseDTO> getMostLikelyByMonth(int month, int year, int page, int size) {
//        long startTime = System.currentTimeMillis();
//        logger.info("Calculating MostLikely baseline for year={}, month={}, page={}, size={}", year, month, page, size);
// 
//        LocalDate firstDay = LocalDate.of(year, month, 1);
//        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
//        String tableName = mostLikelyRevenueJdbcDAO.getTableName(year, month);
//        mostLikelyRevenueJdbcDAO.createTableIfNotExists(tableName);
//
//        Pageable pageable = PageRequest.of(page, size);
// 
//        Page<Baseline> pageData = baselineRepository
//            .findByMostLikelyStartDateLessThanEqualAndMostLikelyEndDateGreaterThanEqual(lastDay, firstDay, pageable);
// 
//        List<MostLikelyResponseDTO> dtoList = pageData.getContent().stream()
//            .map(b -> {
//                MostLikelyResponseDTO dto = new MostLikelyResponseDTO();
//                dto.setAssociateId(b.getAssociateId());
//                dto.setName(b.getAssociateName());
//                dto.setServiceLine(b.getSl());
//                dto.setProjectId(b.getProjectId());
//                dto.setProjectName(b.getProjectDescription());
//                dto.setCurrentStart(b.getMostLikelyStartDate());
//                dto.setCurrentEnd(b.getMostLikelyEndDate());
//                dto.setBillability(b.getBillabilityStatus());
//                dto.setCity(b.getCity());
//                dto.setRate(b.getBillRate());
//                dto.setConfidencePercent(b.getConfidentPercentage());
// 
//                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
//                dto.setHours(hours);
// 
//                LocalDate activeStart = b.getMostLikelyStartDate().isAfter(firstDay) ? b.getMostLikelyStartDate() : firstDay;
//                LocalDate activeEnd = b.getMostLikelyEndDate().isBefore(lastDay) ? b.getMostLikelyEndDate() : lastDay;
// 
//                int workingDays = getWorkingDaysBetween(activeStart, activeEnd);
//                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
//
//                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), activeStart, activeEnd);
// 
//                
//                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);
// 
//                double monthHours = finalWorkingDays * hours;
//                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
//                double baseRevenue = rate * monthHours;
//                double adjustedRevenue = baseRevenue * (b.getConfidentPercentage() / 100.0);
// 
//                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
//                int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
//                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
//                LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
//                LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());
//                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevStart, prevEnd);
// 
//                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
//                double previousMonthHours = finalPrevWorkingDays * hours;
//                double previousRevenue = rate * previousMonthHours;
// 
//                dto.setMonthHours(monthHours);
//                dto.setMonthRevenue(adjustedRevenue);
//                dto.setPreviousRevenue(previousRevenue);
//                dto.setVariance(adjustedRevenue - previousRevenue);
// 
//                return dto;
//            })
//            .collect(Collectors.toList());
// 
//        long endTime = System.currentTimeMillis();
//        logger.info("MostLikely calculation completed in {} ms", endTime - startTime);
// 
//        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
//    }
// 
    
    
    
    @Autowired
    private MostLikelyRevenueJdbcDAO mostLikelyRevenueJdbcDAO;

//    private double round(double value) {
//        return new BigDecimal(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
//    }

    public Page<MostLikelyResponseDTO> getMostLikelyByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating MostLikely revenue for year={}, month={}, page={}, size={}", year, month, page, size);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());

        // 🔹 Prepare dynamic table
        String tableName = mostLikelyRevenueJdbcDAO.getTableName(year, month);
        mostLikelyRevenueJdbcDAO.createTableIfNotExists(tableName);

        List<MostLikelyResponseDTO> allDtos = new ArrayList<>();

        // 🔹 Fetch all matching Baseline records
        List<Baseline> chunk = baselineRepository
            .findByMostLikelyStartDateLessThanEqualAndMostLikelyEndDateGreaterThanEqual(lastDay, firstDay);

        for (Baseline b : chunk) {
            MostLikelyResponseDTO dto = new MostLikelyResponseDTO();
            dto.setAssociateId(b.getAssociateId());
            dto.setName(b.getAssociateName());
            dto.setServiceLine(b.getSl());
            dto.setProjectId(b.getProjectId());
            dto.setProjectName(b.getProjectDescription());
            dto.setCurrentStart(b.getMostLikelyStartDate());
            dto.setCurrentEnd(b.getMostLikelyEndDate());
            dto.setBillability(b.getBillabilityStatus());
            dto.setCity(b.getCity());
            dto.setRate(round(b.getBillRate() != null ? b.getBillRate() : 0));
            dto.setConfidencePercent(b.getConfidentPercentage());

            double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
            dto.setHours(hours);
            dto.setYear(year);
            dto.setMonth(month);

            LocalDate activeStart = b.getMostLikelyStartDate().isAfter(firstDay) ? b.getMostLikelyStartDate() : firstDay;
            LocalDate activeEnd = b.getMostLikelyEndDate().isBefore(lastDay) ? b.getMostLikelyEndDate() : lastDay;

            int workingDays = getWorkingDaysBetween(activeStart, activeEnd);
            int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
            int cognizantHolidays = getCognizantHolidayDays(b.getCity(), activeStart, activeEnd);

            int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);
            double monthHours = finalWorkingDays * hours;
            double rate = dto.getRate();
            double baseRevenue = rate * monthHours;
            double adjustedRevenue = baseRevenue * (dto.getConfidencePercent() / 100.0);
//
//            YearMonth previous = YearMonth.of(year, month).minusMonths(1);
//            LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
//            LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());
//            int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
//            int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
//            int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), prevStart, prevEnd);
//
//            int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
//            double previousMonthHours = finalPrevWorkingDays * hours;
//            double previousRevenue = rate * previousMonthHours;
            
            
            YearMonth previous = YearMonth.of(year, month).minusMonths(1);
            LocalDate prevStart = LocalDate.of(previous.getYear(), previous.getMonthValue(), 1);
            LocalDate prevEnd = prevStart.withDayOfMonth(prevStart.lengthOfMonth());

            double previousRevenue = 0.0;

            // ✅ Check if associate was active in previous month
            if (!b.getMostLikelyStartDate().isAfter(prevEnd) && !b.getMostLikelyEndDate().isBefore(prevStart)) {
                LocalDate activePrevStart = b.getMostLikelyStartDate().isAfter(prevStart) ? b.getMostLikelyStartDate() : prevStart;
                LocalDate activePrevEnd = b.getMostLikelyEndDate().isBefore(prevEnd) ? b.getMostLikelyEndDate() : prevEnd;

                int prevWorkingDays = getWorkingDaysBetween(activePrevStart, activePrevEnd);
                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), activePrevStart, activePrevEnd);

                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
                double previousMonthHours = finalPrevWorkingDays * hours;
                double basePrevRevenue = rate * previousMonthHours;
                previousRevenue = basePrevRevenue * (dto.getConfidencePercent() / 100.0);
            }


            dto.setMonthHours(round(monthHours));
            dto.setMonthRevenue(round(adjustedRevenue));
            dto.setPreviousRevenue(round(previousRevenue));
            dto.setVariance(round(adjustedRevenue - previousRevenue));
            dto.setCalculatedOn(LocalDate.now());

            allDtos.add(dto);
        }

        // 🔹 Store all records in dynamic table
        mostLikelyRevenueJdbcDAO.batchInsertOrUpdate(tableName, allDtos, year, month);

        // 🔹 Paginate final DTO list
        List<MostLikelyResponseDTO> pagedList = allDtos.stream()
            .skip((long) page * size)
            .limit(size)
            .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        logger.info("MostLikely revenue calculation completed in {} ms", endTime - startTime);

        return new PageImpl<>(pagedList, PageRequest.of(page, size), allDtos.size());
    }

    
    private int getWorkingDaysExcludingWeekends(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int workingDays = 0;
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = LocalDate.of(year, month, day);
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDays++;
            }
        }
        return workingDays;
    }
 
    private int getWorkingDaysBetween(LocalDate start, LocalDate end) {
        int workingDays = 0;
        LocalDate date = start;
        while (!date.isAfter(end)) {
            DayOfWeek dow = date.getDayOfWeek();
            if (dow != DayOfWeek.SATURDAY && dow != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            date = date.plusDays(1);
        }
        return workingDays;
    }
 
    private int getAssociateHolidayDays(Integer associateId, int month) {
        return associateHolidayRepository.findById(associateId)
            .map(h -> getMonthValueFromAssociate(h, month))
            .orElse(0);
    }
    
    private int getCognizantHolidayDays(String location, LocalDate activeStart, LocalDate activeEnd) {
        List<CognizantHoliday> holidays = cognizantHolidayRepository
            .findByLocationAndDateBetween(location, activeStart, activeEnd);
        return holidays.size();
    }
 
    private int getMonthValueFromAssociate(AssociateHoliday holiday, int month) {
        return switch (month) {
            case 1 -> holiday.getJan();
            case 2 -> holiday.getFeb();
            case 3 -> holiday.getMar();
            case 4 -> holiday.getApr();
            case 5 -> holiday.getMay();
            case 6 -> holiday.getJun();
            case 7 -> holiday.getJul();
            case 8 -> holiday.getAug();
            case 9 -> holiday.getSep();
            case 10 -> holiday.getOct();
            case 11 -> holiday.getNov();
            case 12 -> holiday.getDec();
            default -> 0;
        };
    }
}