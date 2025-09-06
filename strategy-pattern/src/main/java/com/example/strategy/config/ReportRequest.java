package com.example.strategy.config;

import java.util.Date;
import java.util.List;

/**
 * Represents a report generation request.
 * Used as input for configurable report generation strategies.
 */
public class ReportRequest {
    private final String reportId;
    private final String reportType;
    private final List<String> dataFields;
    private final Date startDate;
    private final Date endDate;
    private final String userId;

    public ReportRequest(String reportId, String reportType, List<String> dataFields, 
                        Date startDate, Date endDate, String userId) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.dataFields = dataFields;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public String getReportId() {
        return reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public List<String> getDataFields() {
        return dataFields;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return String.format("ReportRequest{id='%s', type='%s', fields=%d, dateRange='%s to %s', user='%s'}", 
                           reportId, reportType, dataFields.size(), startDate, endDate, userId);
    }
}