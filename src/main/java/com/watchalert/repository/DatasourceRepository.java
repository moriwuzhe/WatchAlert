package com.watchalert.repository;

import com.watchalert.model.Datasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatasourceRepository extends JpaRepository<Datasource, String> {
    List<Datasource> findByEnabledTrue();
} 