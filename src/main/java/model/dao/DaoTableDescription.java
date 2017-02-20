package model.dao;

import model.domain.TableDescription;

import java.util.List;

public interface DaoTableDescription {
    TableDescription findTableDescriptionByName(String schema, String tableName);
    List<TableDescription> findAllTablesDescription();
    void close();
}
