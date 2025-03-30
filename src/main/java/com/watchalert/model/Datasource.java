package com.watchalert.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "datasources")
public class Datasource {
    @Id
    private String id;
    
    private String name;
    private String type;  // AWS, ALIYUN, etc.
    private String url;
    private String username;
    private String password;
    private String region;
    private String project;
    private String logstore;
    private boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 