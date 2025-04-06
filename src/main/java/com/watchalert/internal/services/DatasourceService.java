package com.watchalert.internal.services;

import com.watchalert.internal.models.Datasource;
import java.util.List;

public interface DatasourceService {
    void initialize();
    void addClientToProviderPools(Datasource datasource) throws Exception;
    List<Datasource> findByType(String type);
    Object getClient(Datasource datasource) throws Exception;
    boolean testConnection(Datasource datasource) throws Exception;
} 