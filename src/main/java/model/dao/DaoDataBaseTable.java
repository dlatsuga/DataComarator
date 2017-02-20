package model.dao;

import model.domain.DataBaseTable;

import java.util.List;


public interface DaoDataBaseTable {
    DataBaseTable findTableByName(String schema, String tableName);
    List<DataBaseTable> findAllTablesOfVnT();
    void close();
}
