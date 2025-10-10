package com.revenueforecast.dao;

import com.revenueforecast.dto.MostLikelyResponseDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MostLikelyRevenueJdbcDAO {

    private final JdbcTemplate jdbcTemplate;

    public MostLikelyRevenueJdbcDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getTableName(int year, int month) {
        return "mostlikely_revenue_" + year + "_" + String.format("%02d", month);
    }

    public boolean tableExists(String tableName) {
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, new Object[]{tableName.toLowerCase()}, Integer.class);
        return count != null && count > 0;
    }

    public void createTableIfNotExists(String tableName) {
        if (!tableExists(tableName)) {
            String sql = "CREATE TABLE " + tableName + " (" +
                    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
                    "associate_id INT," +
                    "associate_name VARCHAR(255)," +
                    "service_line VARCHAR(100)," +
                    "project_id BIGINT," +
                    "project_name VARCHAR(255)," +
                    "city VARCHAR(100)," +
                    "hours DOUBLE," +
                    "confidence_percent DOUBLE," +
                    "year INT," +
                    "month INT," +
                    "month_hours DOUBLE," +
                    "month_revenue DOUBLE," +
                    "previous_revenue DOUBLE," +
                    "variance DOUBLE," +
                    "rate DOUBLE," +
                    "billability VARCHAR(10)," +
                    "start_date DATE," +
                    "end_date DATE," +
                    "calculated_on DATE" +
                    ")";
            jdbcTemplate.execute(sql);
        }
    }

    public void batchInsertOrUpdate(String tableName, List<MostLikelyResponseDTO> dtos, int year, int month) {
        for (MostLikelyResponseDTO dto : dtos) {
            String checkSql = "SELECT COUNT(*) FROM " + tableName + " WHERE associate_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, new Object[]{dto.getAssociateId()}, Integer.class);

            if (count != null && count > 0) {
                String updateSql = "UPDATE " + tableName + " SET " +
                        "service_line = ?, project_id = ?, project_name = ?, city = ?, hours = ?, confidence_percent = ?, " +
                        "month_hours = ?, month_revenue = ?, previous_revenue = ?, variance = ?, rate = ?, billability = ?, " +
                        "start_date = ?, end_date = ?, calculated_on = CURRENT_DATE " +
                        "WHERE associate_id = ?";
                jdbcTemplate.update(updateSql,
                        dto.getServiceLine(),
                        dto.getProjectId(),
                        dto.getProjectName(),
                        dto.getCity(),
                        dto.getHours(),
                        dto.getConfidencePercent(),
                        dto.getMonthHours(),
                        dto.getMonthRevenue(),
                        dto.getPreviousRevenue(),
                        dto.getVariance(),
                        dto.getRate(),
                        dto.getBillability(),
                        dto.getCurrentStart(),
                        dto.getCurrentEnd(),
                        dto.getAssociateId()
                );
            } else {
                String insertSql = "INSERT INTO " + tableName + " (" +
                        "associate_id, associate_name, service_line, project_id, project_name, city, hours, confidence_percent, " +
                        "year, month, month_hours, month_revenue, previous_revenue, variance, rate, billability, " +
                        "start_date, end_date, calculated_on" +
                        ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)";
                jdbcTemplate.update(insertSql,
                        dto.getAssociateId(),
                        dto.getName(),
                        dto.getServiceLine(),
                        dto.getProjectId(),
                        dto.getProjectName(),
                        dto.getCity(),
                        dto.getHours(),
                        dto.getConfidencePercent(),
                        year,
                        month,
                        dto.getMonthHours(),
                        dto.getMonthRevenue(),
                        dto.getPreviousRevenue(),
                        dto.getVariance(),
                        dto.getRate(),
                        dto.getBillability(),
                        dto.getCurrentStart(),
                        dto.getCurrentEnd()
                );
            }
        }
    }
}
