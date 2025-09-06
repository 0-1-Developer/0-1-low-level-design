package com.example.strategy.config;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Represents the result of a report generation operation.
 * Contains the generated report data and metadata.
 */
public class ReportResult {
    private final String reportId;
    private final String format;
    private final String content;
    private final Map<String, Object> metadata;
    private final List<String> warnings;
    private final Date generatedAt;
    private final long generationTimeMs;
    private final boolean success;
    private final String errorMessage;

    private ReportResult(Builder builder) {
        this.reportId = builder.reportId;
        this.format = builder.format;
        this.content = builder.content;
        this.metadata = builder.metadata;
        this.warnings = builder.warnings;
        this.generatedAt = builder.generatedAt;
        this.generationTimeMs = builder.generationTimeMs;
        this.success = builder.success;
        this.errorMessage = builder.errorMessage;
    }

    public String getReportId() {
        return reportId;
    }

    public String getFormat() {
        return format;
    }

    public String getContent() {
        return content;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public Date getGeneratedAt() {
        return generatedAt;
    }

    public long getGenerationTimeMs() {
        return generationTimeMs;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getContentSize() {
        return content != null ? content.length() : 0;
    }

    public boolean hasWarnings() {
        return warnings != null && !warnings.isEmpty();
    }

    @Override
    public String toString() {
        if (success) {
            return String.format("ReportResult{id='%s', format='%s', size=%d chars, time=%dms, warnings=%d}", 
                               reportId, format, getContentSize(), generationTimeMs, 
                               warnings != null ? warnings.size() : 0);
        } else {
            return String.format("ReportResult{id='%s', FAILED after %dms: %s}", 
                               reportId, generationTimeMs, errorMessage);
        }
    }

    public static class Builder {
        private String reportId;
        private String format;
        private String content;
        private Map<String, Object> metadata;
        private List<String> warnings;
        private Date generatedAt = new Date();
        private long generationTimeMs;
        private boolean success = true;
        private String errorMessage;

        public Builder(String reportId) {
            this.reportId = reportId;
        }

        public Builder format(String format) {
            this.format = format;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder metadata(Map<String, Object> metadata) {
            this.metadata = metadata;
            return this;
        }

        public Builder warnings(List<String> warnings) {
            this.warnings = warnings;
            return this;
        }

        public Builder generatedAt(Date generatedAt) {
            this.generatedAt = generatedAt;
            return this;
        }

        public Builder generationTime(long generationTimeMs) {
            this.generationTimeMs = generationTimeMs;
            return this;
        }

        public Builder success(boolean success) {
            this.success = success;
            return this;
        }

        public Builder error(String errorMessage) {
            this.success = false;
            this.errorMessage = errorMessage;
            return this;
        }

        public ReportResult build() {
            return new ReportResult(this);
        }
    }
}