package model.dao;

import com.opencsv.CSVWriter;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
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
        String sql = "{call VT_CREATE_RESULT_TABLES(?,?,?,?,?,?,?,?)}";
        String result = "TEST CREATE RESULT TABLES";

        try{
            CallableStatement callableStatementToCreateResultTables = conn.prepareCall(sql);
            callableStatementToCreateResultTables.setString(1,arrayOfParameters[0]); // case_fields
            callableStatementToCreateResultTables.setString(2,arrayOfParameters[1]); // decode_fields
            callableStatementToCreateResultTables.setString(3,arrayOfParameters[2]); // initial_fields
            callableStatementToCreateResultTables.setString(4,arrayOfParameters[3]); // master join condition
            callableStatementToCreateResultTables.setString(5,arrayOfParameters[4]); // test join condition
            callableStatementToCreateResultTables.setString(6,arrayOfParameters[5]); // group_by_fields
            callableStatementToCreateResultTables.setString(7,arrayOfParameters[6]); // split_fields

            callableStatementToCreateResultTables.registerOutParameter(8, Types.VARCHAR);

            callableStatementToCreateResultTables.executeUpdate();
            result = callableStatementToCreateResultTables.getString(8);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void executeExportQuery(String splitKey) {

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Timestamp(System.currentTimeMillis()));
        exportTotalAnalysisData(timeStamp);
        exportSplitAnalysisData(timeStamp);
        exportTotalCompareData(timeStamp);
        exportSplitCompareData(timeStamp);
        exportTotalExtraMaster(timeStamp);
        exportTotalExtraTest(timeStamp);
//        exportSqlPlusScript();
    }

    private void getUniqueDataForFilter(String sql_base, List<String> listForFilter) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(sql_base);
        ResultSet resultSetOfUniqueDataForFilter = preparedStatement.executeQuery();
        while (resultSetOfUniqueDataForFilter.next()) {
            listForFilter.add(resultSetOfUniqueDataForFilter.getString(1));
        }
    }

    private void writeCsvFile(File file, ResultSet res) throws SQLException {
        try (CSVWriter writer = new CSVWriter(new FileWriter(file), ';')) {
            writer.writeAll(res, true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    private void exportTotalAnalysisData(String timeStamp){
        try {
            String sql = "Select * from VT_ANALYSIS_DTLS ORDER BY Diff";
            Statement statement = conn.createStatement();
            statement.setFetchSize(100000);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_analysis_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void exportSplitAnalysisData(String timeStamp){
        String sql_base = "Select SPLIT_KEY from VT_ANALYSIS_DTLS group by SPLIT_KEY";
        List<String> listForFilter = new ArrayList<>();
        try{
            getUniqueDataForFilter(sql_base, listForFilter);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for (String filterValue : listForFilter) {

            try {
                String sql = "Select * from VT_ANALYSIS_DTLS where SPLIT_KEY = ? ORDER BY Diff";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
                preparedStatement2.setString(1, filterValue);
                preparedStatement2.setFetchSize(100000);

                ResultSet res = preparedStatement2.executeQuery();

                String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\analysis_data\\" + filterValue + "\\" + filterValue + "__analysis_data.csv";
                File file = new File(path);

                file.getParentFile().mkdirs();

                writeCsvFile(file, res);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void exportTotalCompareData(String timeStamp){
        try {
            String sql = "Select * from VT_COMPARE_DTLS ORDER BY Diff";
            Statement statement = conn.createStatement();
            statement.setFetchSize(100000);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_compare_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void exportSplitCompareData(String timeStamp){
        String sql_base = "Select SPLIT_KEY from VT_COMPARE_DTLS group by SPLIT_KEY";
        List<String> listForFilter = new ArrayList<>();
        try{
            getUniqueDataForFilter(sql_base, listForFilter);
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        for (String filterValue : listForFilter) {

            try {
                String sql = "Select * from VT_COMPARE_DTLS where SPLIT_KEY = ? ORDER BY Diff";
                PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
                preparedStatement2.setString(1, filterValue);
                preparedStatement2.setFetchSize(100000);

                ResultSet res = preparedStatement2.executeQuery();

                String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "__compare_data.csv";
                File file = new File(path);

                file.getParentFile().mkdirs();

                writeCsvFile(file, res);
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void exportTotalExtraMaster(String timeStamp){
        try {
            String sql = "Select * from VT_EXTRA_MASTER_DTLS ORDER BY Diff";
            Statement statement = conn.createStatement();
            statement.setFetchSize(100000);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_extra_master_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void exportTotalExtraTest(String timeStamp){
        try {
            String sql = "Select * from VT_EXTRA_TEST_DTLS ORDER BY Diff";
            Statement statement = conn.createStatement();
            statement.setFetchSize(100000);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_extra_test_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
        catch (SQLException e){
            e.printStackTrace();
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
