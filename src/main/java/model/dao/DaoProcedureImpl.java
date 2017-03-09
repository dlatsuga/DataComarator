package model.dao;

import com.opencsv.CSVWriter;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
        String sql_base = "Select BASE_FIELD from VT_ANALYSIS_DTLS group by BASE_FIELD";
//        String sql_base = "Select ? from VT_ANALYSIS_DTLS group by ?";
        List<String> listForFilter = new ArrayList<>();
        try{
            PreparedStatement preparedStatement = conn.prepareStatement(sql_base);
//            preparedStatement.setString(1, splitKey);
//            preparedStatement.setString(2, splitKey);

            ResultSet resultSetOfUniqueDataForFilter = preparedStatement.executeQuery();
            while (resultSetOfUniqueDataForFilter.next()){
                listForFilter.add(resultSetOfUniqueDataForFilter.getString(1));
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for (String filterValue : listForFilter) {
            try {
                String sql = "Select * from VT_ANALYSIS_DTLS where BASE_FIELD = ? ";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
                preparedStatement2.setString(1, filterValue);
                preparedStatement2.setFetchSize(100000);

                ResultSet res = preparedStatement2.executeQuery();

                File file = new File("U:\\DTLS_TMP\\export\\" + filterValue + "\\" + filterValue + "_records.csv");
                file.getParentFile().mkdirs();
                try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
                    writer.writeAll(res, true);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }


    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
