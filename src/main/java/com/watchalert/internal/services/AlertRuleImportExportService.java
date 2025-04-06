package com.watchalert.internal.services;

import com.watchalert.internal.models.AlertRule;
import com.watchalert.internal.models.AlertRuleTemplate;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

public interface AlertRuleImportExportService {
    /**
     * 导出告警规则到JSON文件
     *
     * @param rules 要导出的告警规则列表
     * @param outputStream 输出流
     */
    void exportRules(List<AlertRule> rules, OutputStream outputStream);

    /**
     * 从JSON文件导入告警规则
     *
     * @param inputStream 输入流
     * @return 导入的告警规则列表
     */
    List<AlertRule> importRules(InputStream inputStream);

    /**
     * 导出告警规则模板到JSON文件
     *
     * @param templates 要导出的模板列表
     * @param outputStream 输出流
     */
    void exportTemplates(List<AlertRuleTemplate> templates, OutputStream outputStream);

    /**
     * 从JSON文件导入告警规则模板
     *
     * @param inputStream 输入流
     * @return 导入的模板列表
     */
    List<AlertRuleTemplate> importTemplates(InputStream inputStream);

    /**
     * 验证导入的告警规则数据
     *
     * @param data 要验证的数据
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateRuleData(Map<String, Object> data);

    /**
     * 验证导入的模板数据
     *
     * @param data 要验证的数据
     * @return 验证结果，包含错误信息
     */
    Map<String, Object> validateTemplateData(Map<String, Object> data);
} 