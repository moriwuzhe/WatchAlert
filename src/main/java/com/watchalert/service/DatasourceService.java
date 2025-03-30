package com.watchalert.service;

import com.watchalert.model.Datasource;
import java.util.List;

public interface DatasourceService {
    List<Datasource> getAllEnabledDatasources();
    void addClientToProviderPools(Datasource datasource);
} 