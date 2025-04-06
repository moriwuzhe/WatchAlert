package com.watchalert.api;

import com.watchalert.internal.models.NotificationTemplate;
import com.watchalert.internal.services.NotificationTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notification/templates")
public class NotificationTemplateController {

    @Autowired
    private NotificationTemplateService templateService;

    @GetMapping
    public ResponseEntity<List<NotificationTemplate>> getAllTemplates(
            @RequestParam(required = false) String channel) {
        if (channel != null) {
            return ResponseEntity.ok(templateService.getEnabledTemplates(channel));
        }
        return ResponseEntity.ok(templateService.getEnabledTemplates(null));
    }

    @GetMapping("/{name}")
    public ResponseEntity<NotificationTemplate> getTemplateByName(@PathVariable String name) {
        return ResponseEntity.ok(templateService.getTemplateByName(name));
    }

    @PostMapping
    public ResponseEntity<NotificationTemplate> createTemplate(@RequestBody NotificationTemplate template) {
        return ResponseEntity.ok(templateService.createTemplate(template));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTemplate(
            @PathVariable Long id,
            @RequestBody NotificationTemplate template) {
        template.setId(id);
        templateService.updateTemplate(template);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable Long id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{name}/render")
    public ResponseEntity<String> renderTemplate(
            @PathVariable String name,
            @RequestBody Map<String, Object> data) {
        return ResponseEntity.ok(templateService.renderTemplate(name, data));
    }
} 