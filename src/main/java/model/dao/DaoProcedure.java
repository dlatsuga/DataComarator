package model.dao;


public interface DaoProcedure {
    String callProcedureToCreateBaseTables();
    String callProcedureToUpdateRowNumber(String[] keysValueArray);
    void close();
}
