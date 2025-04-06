package com.watchalert.internal.models;

import lombok.Data;
import javax.persistence.*;
import java.util.Map;

@Data
@Entity
@Table(name = "notification_channels")
public class NotificationChannel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @ElementCollection
    @CollectionTable(name = "notification_channel_configs")
    @MapKeyColumn(name = "config_key")
    @Column(name = "config_value")
    private Map<String, String> config;

    @Column(nullable = false)
    private boolean enabled = true;
} 