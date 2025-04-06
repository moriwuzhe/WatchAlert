package com.watchalert.internal.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseVersion;
import com.watchalert.internal.models.AlertRuleTemplateDependencyTestCaseRelation;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyTestCaseVersionRepository;
import com.watchalert.internal.repositories.AlertRuleTemplateDependencyTestCaseRelationRepository;
import com.watchalert.internal.services.AlertRuleTemplateDependencyTestCaseExportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AlertRuleTemplateDependencyTestCaseExportServiceImpl implements AlertRuleTemplateDependencyTestCaseExportService {

    @Autowired
    private AlertRuleTemplateDependencyTestCaseVersionRepository versionRepository;

    @Autowired
    private AlertRuleTemplateDependencyTestCaseRelationRepository relationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public byte[] exportTestCaseVersion(Long testCaseId, Long versionId, String format) {
        log.debug("Exporting test case version: {}, version: {}, format: {}", testCaseId, versionId, format);
        
        List<AlertRuleTemplateDependencyTestCaseVersion> versions;
        if (versionId != null) {
            versions = Collections.singletonList(versionRepository.findById(versionId)
                    .orElseThrow(() -> new IllegalArgumentException("Version not found: " + versionId)));
        } else {
            versions = versionRepository.findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
        }
        
        return exportData(convertVersionsToMap(versions), format);
    }

    @Override
    public byte[] exportTestCaseDependencies(Long testCaseId, String format) {
        log.debug("Exporting test case dependencies: {}, format: {}", testCaseId, format);
        
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = new ArrayList<>();
        relations.addAll(relationRepository.findBySourceTestCaseId(testCaseId));
        relations.addAll(relationRepository.findByTargetTestCaseId(testCaseId));
        
        return exportData(convertRelationsToMap(relations), format);
    }

    @Override
    public byte[] exportTestCaseVersionHistory(Long testCaseId, String format) {
        log.debug("Exporting test case version history: {}, format: {}", testCaseId, format);
        
        List<AlertRuleTemplateDependencyTestCaseVersion> versions = versionRepository
                .findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
        
        Map<String, Object> historyData = new HashMap<>();
        historyData.put("testCaseId", testCaseId);
        historyData.put("totalVersions", versions.size());
        historyData.put("versions", convertVersionsToMap(versions));
        
        return exportData(historyData, format);
    }

    @Override
    public byte[] exportTestCaseDependencyReport(Long testCaseId, String format) {
        log.debug("Exporting test case dependency report: {}, format: {}", testCaseId, format);
        
        Map<String, Object> reportData = new HashMap<>();
        
        // 获取版本信息
        List<AlertRuleTemplateDependencyTestCaseVersion> versions = versionRepository
                .findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
        reportData.put("versions", convertVersionsToMap(versions));
        
        // 获取依赖关系
        List<AlertRuleTemplateDependencyTestCaseRelation> relations = new ArrayList<>();
        relations.addAll(relationRepository.findBySourceTestCaseId(testCaseId));
        relations.addAll(relationRepository.findByTargetTestCaseId(testCaseId));
        reportData.put("dependencies", convertRelationsToMap(relations));
        
        // 添加统计信息
        reportData.put("totalVersions", versions.size());
        reportData.put("totalDependencies", relations.size());
        
        // 按依赖类型统计
        Map<String, Long> typeDistribution = relations.stream()
                .collect(Collectors.groupingBy(
                        AlertRuleTemplateDependencyTestCaseRelation::getDependencyType,
                        Collectors.counting()
                ));
        reportData.put("dependencyTypeDistribution", typeDistribution);
        
        return exportData(reportData, format);
    }

    @Override
    public byte[] batchExportTestCaseVersions(List<Long> testCaseIds, String format) {
        log.debug("Batch exporting test case versions: {}, format: {}", testCaseIds, format);
        
        Map<String, Object> exportData = new HashMap<>();
        exportData.put("testCaseIds", testCaseIds);
        
        List<Map<String, Object>> allVersions = new ArrayList<>();
        for (Long testCaseId : testCaseIds) {
            List<AlertRuleTemplateDependencyTestCaseVersion> versions = versionRepository
                    .findByTestCaseIdOrderByCreatedAtDesc(testCaseId);
            allVersions.addAll(convertVersionsToMap(versions));
        }
        
        exportData.put("versions", allVersions);
        exportData.put("totalVersions", allVersions.size());
        
        return exportData(exportData, format);
    }

    @Override
    public List<String> getSupportedExportFormats() {
        return Arrays.asList("JSON", "CSV", "EXCEL", "PDF");
    }

    @Override
    public byte[] getExportTemplate(String format) {
        log.debug("Getting export template for format: {}", format);
        
        switch (format.toUpperCase()) {
            case "JSON":
                return getJsonTemplate();
            case "CSV":
                return getCsvTemplate();
            case "EXCEL":
                return getExcelTemplate();
            case "PDF":
                return getPdfTemplate();
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }

    private byte[] exportData(Object data, String format) {
        try {
            switch (format.toUpperCase()) {
                case "JSON":
                    return exportToJson(data);
                case "CSV":
                    return exportToCsv(data);
                case "EXCEL":
                    return exportToExcel(data);
                case "PDF":
                    return exportToPdf(data);
                default:
                    throw new IllegalArgumentException("Unsupported format: " + format);
            }
        } catch (IOException e) {
            log.error("Error exporting data", e);
            throw new RuntimeException("Failed to export data", e);
        }
    }

    private byte[] exportToJson(Object data) throws IOException {
        return objectMapper.writeValueAsBytes(data);
    }

    private byte[] exportToCsv(Object data) throws IOException {
        // 实现CSV导出逻辑
        // 这里需要根据数据结构实现具体的CSV生成逻辑
        return new byte[0];
    }

    private byte[] exportToExcel(Object data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Export Data");
            
            // 创建标题行
            Row headerRow = sheet.createRow(0);
            // 根据数据结构创建相应的列
            // 这里需要根据实际数据结构实现具体的Excel生成逻辑
            
            // 写入数据
            // 这里需要根据实际数据结构实现具体的数据写入逻辑
            
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private byte[] exportToPdf(Object data) throws IOException {
        // 实现PDF导出逻辑
        // 这里需要根据数据结构实现具体的PDF生成逻辑
        return new byte[0];
    }

    private List<Map<String, Object>> convertVersionsToMap(List<AlertRuleTemplateDependencyTestCaseVersion> versions) {
        return versions.stream()
                .map(version -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", version.getId());
                    map.put("testCaseId", version.getTestCaseId());
                    map.put("versionNumber", version.getVersionNumber());
                    map.put("versionType", version.getVersionType());
                    map.put("testData", version.getTestData());
                    map.put("expectedResult", version.getExpectedResult());
                    map.put("testType", version.getTestType());
                    map.put("priority", version.getPriority());
                    map.put("changeDescription", version.getChangeDescription());
                    map.put("createdBy", version.getCreatedBy());
                    map.put("createdAt", version.getCreatedAt());
                    map.put("updatedAt", version.getUpdatedAt());
                    return map;
                })
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertRelationsToMap(List<AlertRuleTemplateDependencyTestCaseRelation> relations) {
        return relations.stream()
                .map(relation -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", relation.getId());
                    map.put("sourceTestCaseId", relation.getSourceTestCaseId());
                    map.put("targetTestCaseId", relation.getTargetTestCaseId());
                    map.put("dependencyType", relation.getDependencyType());
                    map.put("description", relation.getDescription());
                    map.put("createdBy", relation.getCreatedBy());
                    map.put("createdAt", relation.getCreatedAt());
                    map.put("updatedAt", relation.getUpdatedAt());
                    return map;
                })
                .collect(Collectors.toList());
    }

    private byte[] getJsonTemplate() {
        // 返回JSON模板
        return new byte[0];
    }

    private byte[] getCsvTemplate() {
        // 返回CSV模板
        return new byte[0];
    }

    private byte[] getExcelTemplate() {
        // 返回Excel模板
        return new byte[0];
    }

    private byte[] getPdfTemplate() {
        // 返回PDF模板
        return new byte[0];
    }
} 