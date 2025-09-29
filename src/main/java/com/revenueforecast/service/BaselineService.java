package com.revenueforecast.service;

import com.revenueforecast.dto.BaselineResponseDTO;
import com.revenueforecast.dto.MostLikelyResponseDTO;
import com.revenueforecast.model.AssociateHoliday;
import com.revenueforecast.model.Baseline;
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
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BaselineService {

    private static final Logger logger = LoggerFactory.getLogger(BaselineService.class);

    @Autowired private BaselineRepository baselineRepository;
    @Autowired private AssociateHolidayRepository associateHolidayRepository;
    @Autowired private CognizantHolidayRepository cognizantHolidayRepository;

    public Page<BaselineResponseDTO> getBaselineByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating baseline for year={}, month={}, page={}, size={}", year, month, page, size);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Pageable pageable = PageRequest.of(page, size);

        Page<Baseline> pageData = baselineRepository
            .findByCurrentStartDateLessThanEqualAndCurrentEndDateGreaterThanEqual(lastDay, firstDay, pageable);

        List<BaselineResponseDTO> dtoList = pageData.getContent().stream()
            .map(b -> {
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
                dto.setRate(b.getBillRate());

                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
                dto.setHours(hours);

                int workingDays = getWorkingDaysExcludingWeekends(year, month);
                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), year, month);
                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);

                double monthHours = finalWorkingDays * hours;
                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
                double monthRevenue = rate * monthHours;

                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
                int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), previous.getYear(), previous.getMonthValue());
                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
                double previousMonthHours = finalPrevWorkingDays * hours;
                double previousRevenue = rate * previousMonthHours;

                dto.setMonthHours(monthHours);
                dto.setMonthRevenue(monthRevenue);
                dto.setPreviousRevenue(previousRevenue);
                dto.setVariance(monthRevenue - previousRevenue);

                return dto;
            })
            .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        logger.info("Baseline calculation completed in {} ms", endTime - startTime);

        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
    }

    public Page<MostLikelyResponseDTO> getMostLikelyByMonth(int month, int year, int page, int size) {
        long startTime = System.currentTimeMillis();
        logger.info("Calculating MostLikely baseline for year={}, month={}, page={}, size={}", year, month, page, size);

        LocalDate firstDay = LocalDate.of(year, month, 1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        Pageable pageable = PageRequest.of(page, size);

        Page<Baseline> pageData = baselineRepository
            .findByMostLikelyStartDateLessThanEqualAndMostLikelyEndDateGreaterThanEqual(lastDay, firstDay, pageable);

        List<MostLikelyResponseDTO> dtoList = pageData.getContent().stream()
            .map(b -> {
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
                dto.setRate(b.getBillRate());
                dto.setConfidencePercent(b.getConfidentPercentage());

                double hours = b.getCountry().equalsIgnoreCase("India") ? 9.0 : 8.0;
                dto.setHours(hours);

                LocalDate activeStart = b.getMostLikelyStartDate().isAfter(firstDay) ? b.getMostLikelyStartDate() : firstDay;
                LocalDate activeEnd = b.getMostLikelyEndDate().isBefore(lastDay) ? b.getMostLikelyEndDate() : lastDay;

                int workingDays = getWorkingDaysBetween(activeStart, activeEnd);
                int associateHolidays = getAssociateHolidayDays(b.getAssociateId(), month);
                int cognizantHolidays = getCognizantHolidayDays(b.getCity(), year, month);
                
                int finalWorkingDays = Math.max(0, workingDays - associateHolidays - cognizantHolidays);

                double monthHours = finalWorkingDays * hours;
                double rate = b.getBillRate() != null ? b.getBillRate() : 0;
                double baseRevenue = rate * monthHours;
                double adjustedRevenue = baseRevenue * (b.getConfidentPercentage() / 100.0);

                YearMonth previous = YearMonth.of(year, month).minusMonths(1);
                int prevWorkingDays = getWorkingDaysExcludingWeekends(previous.getYear(), previous.getMonthValue());
                int prevAssociateHolidays = getAssociateHolidayDays(b.getAssociateId(), previous.getMonthValue());
                int prevCognizantHolidays = getCognizantHolidayDays(b.getCity(), previous.getYear(), previous.getMonthValue());
                int finalPrevWorkingDays = Math.max(0, prevWorkingDays - prevAssociateHolidays - prevCognizantHolidays);
                double previousMonthHours = finalPrevWorkingDays * hours;
                double previousRevenue = rate * previousMonthHours;

                dto.setMonthHours(monthHours);
                dto.setMonthRevenue(adjustedRevenue);
                dto.setPreviousRevenue(previousRevenue);
                dto.setVariance(adjustedRevenue - previousRevenue);

                return dto;
            })
            .collect(Collectors.toList());

        long endTime = System.currentTimeMillis();
        logger.info("MostLikely calculation completed in {} ms", endTime - startTime);

        return new PageImpl<>(dtoList, pageable, pageData.getTotalElements());
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

    private int getCognizantHolidayDays(String location, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();
        List<CognizantHoliday> holidays = cognizantHolidayRepository.findByLocationAndDateBetween(location, start, end);
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