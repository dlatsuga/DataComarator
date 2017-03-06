package model.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class DaoProcedureImpl implements DaoProcedure {
    Connection conn;

    public DaoProcedureImpl(Connection conn) {
        this.conn = conn;
    }

    public String callProcedureToCreateBaseTables(){
        String sql = "{call VT_CREATE_BASE_TABLES(?)}";
        String result = "TEST CREATE BASE TABLES";
        try
        {
            CallableStatement callableStatementForCreateBaseTables = conn.prepareCall(sql);
            callableStatementForCreateBaseTables.registerOutParameter(1, Types.VARCHAR);
            callableStatementForCreateBaseTables.executeUpdate();
            result = callableStatementForCreateBaseTables.getString(1);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    public String callProcedureToUpdateRowNumber(String rnList, String rnSort){
        String sql = "{call VT_UPDATE_ROW_NUMBER(?,?,?)}";
        String result = "TEST UPDATE ROW NUMBER";
        try {
            CallableStatement callableStatementForUpdateRowNumber = conn.prepareCall(sql);
            callableStatementForUpdateRowNumber.setString(1, rnList);
            callableStatementForUpdateRowNumber.setString(2, rnSort);
            callableStatementForUpdateRowNumber.registerOutParameter(3, Types.VARCHAR);

            callableStatementForUpdateRowNumber.executeUpdate();

            result = callableStatementForUpdateRowNumber.getString(3);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return result;
    }


    public String callProcedureToCreateResultTables(String[] arrayOfParameters) {
        String sql = "{call VT_CREATE_RESULT_TABLES(?,?,?,?,?,?,?)}";
        String result = "TEST CREATE RESULT TABLES";

//        System.out.println(arrayOfParameters[0]);
//        System.out.println(arrayOfParameters[1]);
//        System.out.println(arrayOfParameters[2]);
//        System.out.println(arrayOfParameters[3]);
//        System.out.println(arrayOfParameters[4]);
//        System.out.println(arrayOfParameters[5]);

        try{
            CallableStatement callableStatementToCreateResultTables = conn.prepareCall(sql);
            callableStatementToCreateResultTables.setString(1,arrayOfParameters[0]); // case_fields
            callableStatementToCreateResultTables.setString(2,arrayOfParameters[1]); // decode_fields
            callableStatementToCreateResultTables.setString(3,arrayOfParameters[2]); // initial_fields
            callableStatementToCreateResultTables.setString(4,arrayOfParameters[3]); // master join condition
            callableStatementToCreateResultTables.setString(5,arrayOfParameters[4]); // test join condition
            callableStatementToCreateResultTables.setString(6,arrayOfParameters[5]); // group_by_fields

            callableStatementToCreateResultTables.registerOutParameter(7, Types.VARCHAR);

            callableStatementToCreateResultTables.executeUpdate();
            result = callableStatementToCreateResultTables.getString(7);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void executeExportQuery(String splitKey) {
        String sql =
                "SPOOL \"\\U:\\DTLS_TMP\\CFD.TXT\"\n" +
                "SET sqlformat DELIMITED ; \" \"\n" +
                "SELECT /*delimited*/ * FROM VT_DTLS_TMP tt WHERE tt.secs_instype = 'CFD';\n" +
                "SPOOL OFF";

//        try {
//            Statement statement = conn.createStatement();
//            statement.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        ScriptRunner runner = new ScriptRunner(conn, true, true);
        try {
            runner.runScript(new BufferedReader(new FileReader("U:\\DTLS_TMP\\dtls_tmp.sql")));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//ScriptRunner runner = new ScriptRunner(con, [booleanAutoCommit], [booleanStopOnerror]);
//runner.runScript(new BufferedReader(new FileReader("test.sql")));

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
