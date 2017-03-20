package model.dao;

import com.opencsv.CSVWriter;
import model.domain.DataBaseComparatorConfig;

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

    public String callProcedureToCreateBaseTables(String selectedTableSchema, String selectedTableName) throws SQLException {
        String sql = "{call VT_CREATE_BASE_TABLES(?,?,?,?)}";
        String result = "TEST CREATE BASE TABLES";
        String selectedMasterTableName = selectedTableName;
        String selectedTestTableName = selectedTableName.replaceAll("(?i)master", "TEST");


            CallableStatement callableStatementForCreateBaseTables = conn.prepareCall(sql);
            callableStatementForCreateBaseTables.setString(1, selectedTableSchema);
            callableStatementForCreateBaseTables.setString(2, selectedMasterTableName);
            callableStatementForCreateBaseTables.setString(3, selectedTestTableName);
            callableStatementForCreateBaseTables.registerOutParameter(4, Types.VARCHAR);
            callableStatementForCreateBaseTables.executeUpdate();
            result = callableStatementForCreateBaseTables.getString(4);


        return result;
    }

    public String callProcedureToUpdateRowNumber(String rnList, String rnSort) throws SQLException {
        String sql = "{call VT_UPDATE_ROW_NUMBER(?,?,?)}";
        String result = "TEST UPDATE ROW NUMBER";

            CallableStatement callableStatementForUpdateRowNumber = conn.prepareCall(sql);
            callableStatementForUpdateRowNumber.setString(1, rnList);
            callableStatementForUpdateRowNumber.setString(2, rnSort);
            callableStatementForUpdateRowNumber.registerOutParameter(3, Types.VARCHAR);

            callableStatementForUpdateRowNumber.executeUpdate();

            result = callableStatementForUpdateRowNumber.getString(3);

        return result;
    }

    public String callProcedureToCreateResultTables(String[] arrayOfParameters) throws SQLException {
        String sql = "{call VT_CREATE_RESULT_TABLES(?,?,?,?,?,?,?,?)}";
        String result = "TEST CREATE RESULT TABLES";

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

        return result;
    }

    public void executeExportQuery(String splitKey, DataBaseComparatorConfig dataBaseComparatorConfig) throws SQLException {

        String timeStamp = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Timestamp(System.currentTimeMillis()));
        int fetchSize = dataBaseComparatorConfig.getFetchSize();

        if(dataBaseComparatorConfig.isExportTotalAnalysisData())

            System.out.println(dataBaseComparatorConfig.isExportTotalAnalysisData());

            exportTotalAnalysisData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitAnalysisData())

            System.out.println(dataBaseComparatorConfig.isExportTotalAnalysisData());

            exportSplitAnalysisData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportTotalCompareData())
            exportTotalCompareData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitCompareData())
            exportSplitCompareData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportTotalExtraMaster())
            exportTotalExtraMaster(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportTotalExtraTest())
            exportTotalExtraTest(timeStamp, fetchSize);
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

    private void exportTotalAnalysisData(String timeStamp, int fetchSize) throws SQLException {

            String sql = "SELECT * FROM(\n" +
                            "SELECT \n" +
                            "   tt.COLUMN_ID\n" +
                            "  ,tt.column_name\n" +
                            "  ,REC_SUM(tt.column_name, tt.table_name, tt.owner, 'All') sum_\n" +
                            "FROM all_tab_columns tt \n" +
                            "WHERE tt.table_name = upper('VT_ANALYSIS_DTLS') and tt.COLUMN_ID > 1)\n" +
                            "WHERE sum_ > 0\n" +
                            "ORDER BY COLUMN_ID";
            Statement statement = conn.createStatement();
            ResultSet resBase = statement.executeQuery(sql);
            StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Diff");
            while (resBase.next()){
                stringBuilder.append(", ");
                stringBuilder.append(resBase.getString(2));
            }

            statement.setFetchSize(fetchSize);
            sql = stringBuilder.toString();
            sql = "Select " + sql + " from VT_ANALYSIS_DTLS ORDER BY Diff, M_TECH_KEY";

            System.out.println(sql);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_analysis_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
    }

    private void exportSplitAnalysisData(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY from VT_ANALYSIS_DTLS group by SPLIT_KEY";
        List<String> listForFilter = new ArrayList<>();

            getUniqueDataForFilter(sql_base, listForFilter);

        for (String filterValue : listForFilter) {
            String sqlFields = "SELECT * FROM(\n" +
                    "SELECT \n" +
                    "   tt.COLUMN_ID\n" +
                    "  ,tt.column_name\n" +
                    "  ,REC_SUM(tt.column_name, tt.table_name, tt.owner, '" + filterValue + "') sum_\n" +
                    "FROM all_tab_columns tt \n" +
                    "WHERE tt.table_name = upper('VT_ANALYSIS_DTLS') and tt.COLUMN_ID > 1)\n" +
                    "WHERE sum_ > 0\n" +
                    "ORDER BY COLUMN_ID";
            Statement statement = conn.createStatement();
            ResultSet resBase = statement.executeQuery(sqlFields);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Diff");
            while (resBase.next()){
                stringBuilder.append(", ");
                stringBuilder.append(resBase.getString(2));
            }
            String fieldForQuery = stringBuilder.toString();
            if(fieldForQuery.equals("Diff")){
                fieldForQuery = "*";
            }
            String sql = "Select " + fieldForQuery + " from VT_ANALYSIS_DTLS where SPLIT_KEY = ? ORDER BY Diff, M_TECH_KEY";

            System.out.println(sql);

            PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
            preparedStatement2.setString(1, filterValue);
            preparedStatement2.setFetchSize(fetchSize);

            ResultSet res = preparedStatement2.executeQuery();

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\analysis_data\\" + filterValue + "\\" + filterValue + "__analysis_data.csv";
            File file = new File(path);

            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
    }

    private void exportTotalCompareData(String timeStamp, int fetchSize) throws SQLException {
            String sql = "Select * from VT_COMPARE ORDER BY Diff, M_TECH_KEY";
            Statement statement = conn.createStatement();
            statement.setFetchSize(fetchSize);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_compare_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
    }

    private void exportSplitCompareData(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY from VT_COMPARE group by SPLIT_KEY";
        List<String> listForFilter = new ArrayList<>();

            getUniqueDataForFilter(sql_base, listForFilter);

        for (String filterValue : listForFilter) {

            String sql = "Select * from VT_COMPARE where SPLIT_KEY = ? ORDER BY Diff, M_TECH_KEY";
            PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
            preparedStatement2.setString(1, filterValue);
            preparedStatement2.setFetchSize(fetchSize);

            ResultSet res = preparedStatement2.executeQuery();

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "__compare_data.csv";
            File file = new File(path);

            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
    }

    private void exportTotalExtraMaster(String timeStamp, int fetchSize) throws SQLException {
            String sql = "Select * from VT_EXTRA_MASTER ORDER BY Diff, M_TECH_KEY";
            Statement statement = conn.createStatement();
            statement.setFetchSize(fetchSize);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_extra_master_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
    }

    private void exportTotalExtraTest(String timeStamp, int fetchSize) throws SQLException {
            String sql = "Select * from VT_EXTRA_TEST ORDER BY Diff, M_TECH_KEY";
            Statement statement = conn.createStatement();
            statement.setFetchSize(fetchSize);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_extra_test_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
