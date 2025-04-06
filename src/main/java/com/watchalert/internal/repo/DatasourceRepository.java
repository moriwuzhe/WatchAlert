package com.watchalert.internal.repo;

import com.watchalert.internal.models.Datasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DatasourceRepository extends JpaRepository<Datasource, Long> {
    List<Datasource> findByType(String type);
    List<Datasource> findByEnabled(boolean enabled);
} 