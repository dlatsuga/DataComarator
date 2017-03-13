package model.dao;


public interface DaoProcedure {
    String callProcedureToCreateBaseTables(String selectedTableSchema, String selectedTableName);
    String callProcedureToUpdateRowNumber(String rnList, String rnSort);
    String callProcedureToCreateResultTables(String[] arrayOfParameters);
    void executeExportQuery(String splitKey);
    void close();
}
