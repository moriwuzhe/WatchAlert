package com.watchalert.internal.services.impl;

import com.watchalert.internal.models.NotificationTemplate;
import com.watchalert.internal.repo.NotificationTemplateRepository;
import com.watchalert.internal.services.NotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class NotificationTemplateServiceImpl implements NotificationTemplateService {

    @Autowired
    private NotificationTemplateRepository templateRepository;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)}");

    @Override
    public NotificationTemplate createTemplate(NotificationTemplate template) {
        template.setEnabled(true);
        return templateRepository.save(template);
    }

    @Override
    public void updateTemplate(NotificationTemplate template) {
        templateRepository.save(template);
    }

    @Override
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    @Override
    public List<NotificationTemplate> getEnabledTemplates(String channel) {
        return templateRepository.findByChannelAndEnabled(channel, true);
    }

    @Override
    public NotificationTemplate getTemplateByName(String name) {
        return templateRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Template not found: " + name));
    }

    @Override
    public String renderTemplate(String templateName, Object data) {
        NotificationTemplate template = getTemplateByName(templateName);
        String content = template.getContent();
        
        // 将数据对象转换为Map
        Map<String, Object> dataMap = convertToMap(data);
        
        // 替换模板变量
        Matcher matcher = VARIABLE_PATTERN.matcher(content);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String variable = matcher.group(1);
            String[] parts = variable.split("\\.");
            Object value = dataMap;
            
            // 处理嵌套属性
            for (String part : parts) {
                if (value instanceof Map) {
                    value = ((Map<?, ?>) value).get(part);
                } else {
                    value = null;
                    break;
                }
            }
            
            // 替换变量
            matcher.appendReplacement(result, value != null ? value.toString() : "");
        }
        
        matcher.appendTail(result);
        return result.toString();
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> convertToMap(Object obj) {
        if (obj instanceof Map) {
            return (Map<String, Object>) obj;
        }
        
        try {
            // 使用反射获取对象的所有字段
            Map<String, Object> map = new java.util.HashMap<>();
            for (java.lang.reflect.Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
            return map;
        } catch (Exception e) {
            log.error("Failed to convert object to map", e);
            return new java.util.HashMap<>();
        }
    }
} 