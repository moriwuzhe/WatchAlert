package com.watchalert.internal.models;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String resource;
    private String action;
    private Boolean enabled;
} 