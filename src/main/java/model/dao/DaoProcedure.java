package model.dao;


import model.domain.DataBaseComparatorConfig;

import java.sql.SQLException;

public interface DaoProcedure {
    String callProcedureToCreateBaseTables(String selectedTableSchema, String selectedTableName) throws SQLException;
    String callProcedureToUpdateRowNumber(String rnList, String rnSort) throws SQLException;
    String callProcedureToCreateResultTables(String[] arrayOfParameters) throws SQLException;
    void executeExportQuery(String splitKey, DataBaseComparatorConfig dataBaseComparatorConfig) throws SQLException;
    void close();
}
