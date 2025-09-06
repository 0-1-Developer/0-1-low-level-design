package com.example.strategy.config;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Collection of configurable report generation strategies.
 * Each strategy can be customized through configuration parameters.
 */
public class ReportGenerationStrategies {

    /**
     * CSV report generation strategy
     */
    public static final ConfigurableStrategy<ReportRequest, ReportResult, StrategyConfig> CSV_GENERATOR = 
        (request, config) -> {
            long startTime = System.currentTimeMillis();
            
            try {
                String delimiter = config.getString("delimiter", ",");
                boolean includeHeaders = config.getBoolean("include_headers", true);
                String dateFormat = config.getString("date_format", "yyyy-MM-dd");
                int maxRows = config.getInt("max_rows", 1000);
                
                StringBuilder csvContent = new StringBuilder();
                
                // Add headers if configured
                if (includeHeaders) {
                    String headers = String.join(delimiter, request.getDataFields());
                    csvContent.append(headers).append("\n");
                }
                
                // Generate sample data based on configuration
                Random random = new Random();
                int actualRows = Math.min(maxRows, random.nextInt(50) + 10);
                
                for (int i = 0; i < actualRows; i++) {
                    List<String> row = new ArrayList<>();
                    for (String field : request.getDataFields()) {
                        row.add(generateSampleValue(field, random));
                    }
                    csvContent.append(String.join(delimiter, row)).append("\n");
                }
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("delimiter", delimiter);
                metadata.put("row_count", actualRows);
                metadata.put("column_count", request.getDataFields().size());
                metadata.put("date_format", dateFormat);
                
                return new ReportResult.Builder(request.getReportId())
                    .format("CSV")
                    .content(csvContent.toString())
                    .metadata(metadata)
                    .generationTime(System.currentTimeMillis() - startTime)
                    .build();
                    
            } catch (Exception e) {
                return new ReportResult.Builder(request.getReportId())
                    .generationTime(System.currentTimeMillis() - startTime)
                    .error("CSV generation failed: " + e.getMessage())
                    .build();
            }
        };

    /**
     * JSON report generation strategy
     */
    public static final ConfigurableStrategy<ReportRequest, ReportResult, StrategyConfig> JSON_GENERATOR = 
        (request, config) -> {
            long startTime = System.currentTimeMillis();
            
            try {
                boolean prettyPrint = config.getBoolean("pretty_print", false);
                boolean includeMetadata = config.getBoolean("include_metadata", true);
                int maxRecords = config.getInt("max_records", 500);
                String rootElement = config.getString("root_element", "data");
                
                StringBuilder jsonContent = new StringBuilder();
                String indent = prettyPrint ? "  " : "";
                String newline = prettyPrint ? "\n" : "";
                
                jsonContent.append("{").append(newline);
                
                if (includeMetadata) {
                    jsonContent.append(indent).append("\"metadata\": {").append(newline);
                    jsonContent.append(indent).append(indent).append("\"report_id\": \"").append(request.getReportId()).append("\",").append(newline);
                    jsonContent.append(indent).append(indent).append("\"report_type\": \"").append(request.getReportType()).append("\",").append(newline);
                    jsonContent.append(indent).append(indent).append("\"generated_by\": \"").append(request.getUserId()).append("\"").append(newline);
                    jsonContent.append(indent).append("},").append(newline);
                }
                
                jsonContent.append(indent).append("\"").append(rootElement).append("\": [").append(newline);
                
                Random random = new Random();
                int actualRecords = Math.min(maxRecords, random.nextInt(30) + 5);
                
                for (int i = 0; i < actualRecords; i++) {
                    jsonContent.append(indent).append(indent).append("{").append(newline);
                    for (int j = 0; j < request.getDataFields().size(); j++) {
                        String field = request.getDataFields().get(j);
                        String value = generateSampleValue(field, random);
                        jsonContent.append(indent).append(indent).append(indent)
                                  .append("\"").append(field).append("\": \"").append(value).append("\"");
                        if (j < request.getDataFields().size() - 1) {
                            jsonContent.append(",");
                        }
                        jsonContent.append(newline);
                    }
                    jsonContent.append(indent).append(indent).append("}");
                    if (i < actualRecords - 1) {
                        jsonContent.append(",");
                    }
                    jsonContent.append(newline);
                }
                
                jsonContent.append(indent).append("]").append(newline);
                jsonContent.append("}");
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("pretty_print", prettyPrint);
                metadata.put("record_count", actualRecords);
                metadata.put("include_metadata", includeMetadata);
                
                return new ReportResult.Builder(request.getReportId())
                    .format("JSON")
                    .content(jsonContent.toString())
                    .metadata(metadata)
                    .generationTime(System.currentTimeMillis() - startTime)
                    .build();
                    
            } catch (Exception e) {
                return new ReportResult.Builder(request.getReportId())
                    .generationTime(System.currentTimeMillis() - startTime)
                    .error("JSON generation failed: " + e.getMessage())
                    .build();
            }
        };

    /**
     * HTML report generation strategy
     */
    public static final ConfigurableStrategy<ReportRequest, ReportResult, StrategyConfig> HTML_GENERATOR = 
        (request, config) -> {
            long startTime = System.currentTimeMillis();
            
            try {
                String theme = config.getString("theme", "default");
                boolean includeCSS = config.getBoolean("include_css", true);
                String title = config.getString("title", "Generated Report");
                boolean showRowNumbers = config.getBoolean("show_row_numbers", false);
                int maxRows = config.getInt("max_rows", 200);
                
                StringBuilder htmlContent = new StringBuilder();
                
                htmlContent.append("<!DOCTYPE html>\n<html>\n<head>\n");
                htmlContent.append("<title>").append(title).append("</title>\n");
                
                if (includeCSS) {
                    htmlContent.append("<style>\n");
                    if ("bootstrap".equals(theme)) {
                        htmlContent.append("table { border-collapse: collapse; width: 100%; }\n");
                        htmlContent.append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }\n");
                        htmlContent.append("th { background-color: #f2f2f2; }\n");
                    } else {
                        htmlContent.append("table { border-collapse: collapse; width: 100%; font-family: Arial, sans-serif; }\n");
                        htmlContent.append("th, td { border: 1px solid black; padding: 5px; }\n");
                        htmlContent.append("th { background-color: #cccccc; }\n");
                    }
                    htmlContent.append("</style>\n");
                }
                
                htmlContent.append("</head>\n<body>\n");
                htmlContent.append("<h1>").append(title).append("</h1>\n");
                htmlContent.append("<table>\n<thead>\n<tr>\n");
                
                if (showRowNumbers) {
                    htmlContent.append("<th>#</th>\n");
                }
                
                for (String field : request.getDataFields()) {
                    htmlContent.append("<th>").append(field).append("</th>\n");
                }
                
                htmlContent.append("</tr>\n</thead>\n<tbody>\n");
                
                Random random = new Random();
                int actualRows = Math.min(maxRows, random.nextInt(40) + 5);
                
                for (int i = 0; i < actualRows; i++) {
                    htmlContent.append("<tr>\n");
                    
                    if (showRowNumbers) {
                        htmlContent.append("<td>").append(i + 1).append("</td>\n");
                    }
                    
                    for (String field : request.getDataFields()) {
                        String value = generateSampleValue(field, random);
                        htmlContent.append("<td>").append(value).append("</td>\n");
                    }
                    
                    htmlContent.append("</tr>\n");
                }
                
                htmlContent.append("</tbody>\n</table>\n");
                htmlContent.append("</body>\n</html>");
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("theme", theme);
                metadata.put("row_count", actualRows);
                metadata.put("include_css", includeCSS);
                metadata.put("show_row_numbers", showRowNumbers);
                
                return new ReportResult.Builder(request.getReportId())
                    .format("HTML")
                    .content(htmlContent.toString())
                    .metadata(metadata)
                    .generationTime(System.currentTimeMillis() - startTime)
                    .build();
                    
            } catch (Exception e) {
                return new ReportResult.Builder(request.getReportId())
                    .generationTime(System.currentTimeMillis() - startTime)
                    .error("HTML generation failed: " + e.getMessage())
                    .build();
            }
        };

    /**
     * XML report generation strategy
     */
    public static final ConfigurableStrategy<ReportRequest, ReportResult, StrategyConfig> XML_GENERATOR = 
        (request, config) -> {
            long startTime = System.currentTimeMillis();
            
            try {
                boolean includeXmlDeclaration = config.getBoolean("include_xml_declaration", true);
                String rootElement = config.getString("root_element", "report");
                String recordElement = config.getString("record_element", "record");
                boolean prettyFormat = config.getBoolean("pretty_format", true);
                int maxRecords = config.getInt("max_records", 300);
                
                StringBuilder xmlContent = new StringBuilder();
                String indent = prettyFormat ? "  " : "";
                String newline = prettyFormat ? "\n" : "";
                
                if (includeXmlDeclaration) {
                    xmlContent.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(newline);
                }
                
                xmlContent.append("<").append(rootElement).append(" id=\"").append(request.getReportId()).append("\">").append(newline);
                
                Random random = new Random();
                int actualRecords = Math.min(maxRecords, random.nextInt(25) + 5);
                
                for (int i = 0; i < actualRecords; i++) {
                    xmlContent.append(indent).append("<").append(recordElement).append(">").append(newline);
                    
                    for (String field : request.getDataFields()) {
                        String value = generateSampleValue(field, random);
                        xmlContent.append(indent).append(indent)
                                  .append("<").append(field).append(">")
                                  .append(value)
                                  .append("</").append(field).append(">")
                                  .append(newline);
                    }
                    
                    xmlContent.append(indent).append("</").append(recordElement).append(">").append(newline);
                }
                
                xmlContent.append("</").append(rootElement).append(">");
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("root_element", rootElement);
                metadata.put("record_element", recordElement);
                metadata.put("record_count", actualRecords);
                metadata.put("pretty_format", prettyFormat);
                
                return new ReportResult.Builder(request.getReportId())
                    .format("XML")
                    .content(xmlContent.toString())
                    .metadata(metadata)
                    .generationTime(System.currentTimeMillis() - startTime)
                    .build();
                    
            } catch (Exception e) {
                return new ReportResult.Builder(request.getReportId())
                    .generationTime(System.currentTimeMillis() - startTime)
                    .error("XML generation failed: " + e.getMessage())
                    .build();
            }
        };

    /**
     * Summary report generation strategy that aggregates data
     */
    public static final ConfigurableStrategy<ReportRequest, ReportResult, StrategyConfig> SUMMARY_GENERATOR = 
        (request, config) -> {
            long startTime = System.currentTimeMillis();
            
            try {
                String summaryType = config.getString("summary_type", "basic");
                boolean includeStatistics = config.getBoolean("include_statistics", true);
                boolean includeCharts = config.getBoolean("include_charts", false);
                
                StringBuilder summaryContent = new StringBuilder();
                
                summaryContent.append("REPORT SUMMARY\n");
                summaryContent.append("==============\n\n");
                summaryContent.append("Report ID: ").append(request.getReportId()).append("\n");
                summaryContent.append("Report Type: ").append(request.getReportType()).append("\n");
                summaryContent.append("Generated for: ").append(request.getUserId()).append("\n");
                summaryContent.append("Date Range: ").append(request.getStartDate()).append(" to ").append(request.getEndDate()).append("\n\n");
                
                summaryContent.append("Data Fields (").append(request.getDataFields().size()).append("):\n");
                for (int i = 0; i < request.getDataFields().size(); i++) {
                    summaryContent.append("  ").append(i + 1).append(". ").append(request.getDataFields().get(i)).append("\n");
                }
                summaryContent.append("\n");
                
                if (includeStatistics) {
                    Random random = new Random();
                    summaryContent.append("STATISTICS\n");
                    summaryContent.append("----------\n");
                    summaryContent.append("Total Records: ").append(random.nextInt(1000) + 100).append("\n");
                    summaryContent.append("Processing Time: ").append(random.nextInt(5000) + 500).append(" ms\n");
                    summaryContent.append("Data Quality Score: ").append(random.nextInt(40) + 60).append("%\n\n");
                }
                
                if (includeCharts) {
                    summaryContent.append("CHARTS\n");
                    summaryContent.append("------\n");
                    summaryContent.append("[Bar Chart: Data Distribution]\n");
                    summaryContent.append("[Line Chart: Trend Analysis]\n");
                    summaryContent.append("[Pie Chart: Category Breakdown]\n\n");
                }
                
                if ("detailed".equals(summaryType)) {
                    summaryContent.append("DETAILED ANALYSIS\n");
                    summaryContent.append("-----------------\n");
                    summaryContent.append("This report provides comprehensive insights into the requested data.\n");
                    summaryContent.append("Key findings and recommendations will be included in the full report.\n");
                }
                
                Map<String, Object> metadata = new HashMap<>();
                metadata.put("summary_type", summaryType);
                metadata.put("include_statistics", includeStatistics);
                metadata.put("include_charts", includeCharts);
                
                return new ReportResult.Builder(request.getReportId())
                    .format("SUMMARY")
                    .content(summaryContent.toString())
                    .metadata(metadata)
                    .generationTime(System.currentTimeMillis() - startTime)
                    .build();
                    
            } catch (Exception e) {
                return new ReportResult.Builder(request.getReportId())
                    .generationTime(System.currentTimeMillis() - startTime)
                    .error("Summary generation failed: " + e.getMessage())
                    .build();
            }
        };

    /**
     * Helper method to generate sample data based on field name
     */
    private static String generateSampleValue(String field, Random random) {
        String lowerField = field.toLowerCase();
        
        if (lowerField.contains("name")) {
            String[] names = {"John", "Jane", "Alice", "Bob", "Charlie", "Diana", "Edward", "Fiona"};
            return names[random.nextInt(names.length)];
        } else if (lowerField.contains("email")) {
            String[] domains = {"example.com", "test.org", "sample.net", "demo.co"};
            return "user" + random.nextInt(1000) + "@" + domains[random.nextInt(domains.length)];
        } else if (lowerField.contains("id")) {
            return "ID" + (random.nextInt(9000) + 1000);
        } else if (lowerField.contains("amount") || lowerField.contains("price") || lowerField.contains("cost")) {
            return String.format("%.2f", random.nextDouble() * 1000);
        } else if (lowerField.contains("date")) {
            return "2024-" + String.format("%02d", random.nextInt(12) + 1) + "-" + String.format("%02d", random.nextInt(28) + 1);
        } else if (lowerField.contains("status")) {
            String[] statuses = {"Active", "Inactive", "Pending", "Completed", "Failed"};
            return statuses[random.nextInt(statuses.length)];
        } else {
            // Generic data
            return "Value" + random.nextInt(1000);
        }
    }
}