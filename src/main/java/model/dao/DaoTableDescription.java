package model.dao;

import model.domain.TableDescription;

import java.util.List;
import java.util.Set;


public interface DaoTableDescription {
    TableDescription findTableDescriptionByName(String schema, String tableName);
    List<TableDescription> findAllTablesDescription(boolean isUniqueCnt);
    Set<String> getSetOfTableKey();
    void close();
}
