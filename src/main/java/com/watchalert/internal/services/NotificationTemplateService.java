package com.watchalert.internal.services;

import com.watchalert.internal.models.NotificationTemplate;
import java.util.List;

public interface NotificationTemplateService {
    /**
     * 创建新的通知模板
     *
     * @param template 通知模板
     * @return 创建的通知模板
     */
    NotificationTemplate createTemplate(NotificationTemplate template);

    /**
     * 更新通知模板
     *
     * @param template 通知模板
     */
    void updateTemplate(NotificationTemplate template);

    /**
     * 删除通知模板
     *
     * @param id 模板ID
     */
    void deleteTemplate(Long id);

    /**
     * 获取指定渠道的已启用模板列表
     *
     * @param channel 通知渠道
     * @return 模板列表
     */
    List<NotificationTemplate> getEnabledTemplates(String channel);

    /**
     * 根据名称获取模板
     *
     * @param name 模板名称
     * @return 模板
     */
    NotificationTemplate getTemplateByName(String name);

    /**
     * 渲染模板
     *
     * @param templateName 模板名称
     * @param data 模板数据
     * @return 渲染后的内容
     */
    String renderTemplate(String templateName, Object data);
} 