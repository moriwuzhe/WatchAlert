package com.watchalert.internal.repo;

import com.watchalert.internal.models.NotificationTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    List<NotificationTemplate> findByChannelAndEnabled(String channel, boolean enabled);
    Optional<NotificationTemplate> findByName(String name);
} 