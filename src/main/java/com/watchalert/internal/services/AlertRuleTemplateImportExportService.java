package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRuleTemplate;
import java.util.List;
import java.util.Map;

public interface AlertRuleTemplateImportExportService {
    
    /**
     * 导出模板
     *
     * @param templateId 模板ID
     * @return 导出的模板数据
     */
    Map<String, Object> exportTemplate(Long templateId);
    
    /**
     * 导出多个模板
     *
     * @param templateIds 模板ID列表
     * @return 导出的模板数据列表
     */
    List<Map<String, Object>> exportTemplates(List<Long> templateIds);
    
    /**
     * 导出模板为JSON字符串
     *
     * @param templateId 模板ID
     * @return JSON字符串
     */
    String exportTemplateAsJson(Long templateId);
    
    /**
     * 导出多个模板为JSON字符串
     *
     * @param templateIds 模板ID列表
     * @return JSON字符串
     */
    String exportTemplatesAsJson(List<Long> templateIds);
    
    /**
     * 导入模板
     *
     * @param templateData 模板数据
     * @param createdBy 创建人
     * @return 导入的模板
     */
    AlertRuleTemplate importTemplate(Map<String, Object> templateData, String createdBy);
    
    /**
     * 导入多个模板
     *
     * @param templatesData 模板数据列表
     * @param createdBy 创建人
     * @return 导入的模板列表
     */
    List<AlertRuleTemplate> importTemplates(List<Map<String, Object>> templatesData, String createdBy);
    
    /**
     * 从JSON字符串导入模板
     *
     * @param jsonData JSON字符串
     * @param createdBy 创建人
     * @return 导入的模板
     */
    AlertRuleTemplate importTemplateFromJson(String jsonData, String createdBy);
    
    /**
     * 从JSON字符串导入多个模板
     *
     * @param jsonData JSON字符串
     * @param createdBy 创建人
     * @return 导入的模板列表
     */
    List<AlertRuleTemplate> importTemplatesFromJson(String jsonData, String createdBy);
    
    /**
     * 验证模板数据
     *
     * @param templateData 模板数据
     * @return 验证结果
     */
    Map<String, Object> validateTemplateData(Map<String, Object> templateData);
    
    /**
     * 获取导入进度
     *
     * @param importId 导入ID
     * @return 导入进度信息
     */
    Map<String, Object> getImportProgress(String importId);
    
    /**
     * 获取导出进度
     *
     * @param exportId 导出ID
     * @return 导出进度信息
     */
    Map<String, Object> getExportProgress(String exportId);
} 