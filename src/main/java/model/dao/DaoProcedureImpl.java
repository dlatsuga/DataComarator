package model.dao;

import com.opencsv.CSVWriter;
import model.domain.DataBaseComparatorConfig;

import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
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

        /*Analysis*/
        if(dataBaseComparatorConfig.isExportTotalAnalysisData())
            exportTotalAnalysisData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitAnalysisData())
            exportSplitAnalysisData(timeStamp, fetchSize);

        /*Compare*/
        if(dataBaseComparatorConfig.isExportTotalCompareData())
            exportTotalCompareData(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitCompareData())
            exportSplitCompareData(timeStamp, fetchSize);

        /*ExtraMaster*/
        if(dataBaseComparatorConfig.isExportTotalExtraMaster())
            exportTotalExtraMaster(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitExtraMaster())
            exportSplitExtraMaster(timeStamp, fetchSize);

        /*ExtraTest*/
        if(dataBaseComparatorConfig.isExportTotalExtraTest())
            exportTotalExtraTest(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitExtraTest())
            exportSplitExtraTest(timeStamp, fetchSize);

        /*Deviations*/
        if(dataBaseComparatorConfig.isExportTotalDeviationsData())
            exportTotalDeviations(timeStamp, fetchSize);
        if(dataBaseComparatorConfig.isExportSplitDeviationsData())
            exportSplitDeviations(timeStamp, fetchSize);

        /*OriginalMaster*/
        if(dataBaseComparatorConfig.isExportTotalOriginalMaster())
            exportTotalOriginalMaster(timeStamp, fetchSize, splitKey);
        if(dataBaseComparatorConfig.isExportSplitOriginalMaster())
            exportSplitOriginalMaster(timeStamp, fetchSize, splitKey);

        /*OriginalTest*/
        if(dataBaseComparatorConfig.isExportTotalOriginalTest())
            exportTotalOriginalTest(timeStamp, fetchSize, splitKey);
        if(dataBaseComparatorConfig.isExportSplitOriginalTest())
            exportSplitOriginalTest(timeStamp, fetchSize, splitKey);

    }

    private void getUniqueDataForFilter(String sql_base, HashMap<String, Integer> mapForFilter) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(sql_base);
        ResultSet resultSetOfUniqueDataForFilter = preparedStatement.executeQuery();
        while (resultSetOfUniqueDataForFilter.next()) {
            mapForFilter.put(resultSetOfUniqueDataForFilter.getString(1), resultSetOfUniqueDataForFilter.getInt(2));
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
                            "WHERE tt.table_name = upper('VT_ANALYSIS') and tt.COLUMN_ID > 1)\n" +
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

            String fieldForQuery = stringBuilder.toString();
            if(fieldForQuery.equals("Diff")){
                fieldForQuery = "*";
            }
            sql = "Select " + fieldForQuery + " from VT_ANALYSIS ORDER BY Diff, M_TECH_KEY";

            statement.setFetchSize(fetchSize);
            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_analysis_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);

        System.out.println("exportTotalAnalysisData");
    }
    private void exportSplitAnalysisData(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY, sum(DIFF) sum_ from VT_ANALYSIS group by SPLIT_KEY";
        HashMap<String, Integer> mapForFilter= new HashMap<>();

            getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sqlFields = "SELECT * FROM(\n" +
                    "SELECT \n" +
                    "   tt.COLUMN_ID\n" +
                    "  ,tt.column_name\n" +
                    "  ,REC_SUM(tt.column_name, tt.table_name, tt.owner, '" + filterValue + "') sum_\n" +
                    "FROM all_tab_columns tt \n" +
                    "WHERE tt.table_name = upper('VT_ANALYSIS') and tt.COLUMN_ID > 1)\n" +
                    "WHERE sum_ > 0\n" +
                    "ORDER BY COLUMN_ID";
            Statement statement = conn.createStatement();
            ResultSet resBase = statement.executeQuery(sqlFields);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DIFF");
            while (resBase.next()){
                stringBuilder.append(", ");
                stringBuilder.append(resBase.getString(2));
            }
            String fieldForQuery = stringBuilder.toString();
            if(fieldForQuery.equals("DIFF")){
                fieldForQuery = "*";
            }
            String sql = "Select " + fieldForQuery + " from VT_ANALYSIS where SPLIT_KEY = ? ORDER BY DIFF, M_TECH_KEY";

            PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
            preparedStatement2.setString(1, filterValue);
            preparedStatement2.setFetchSize(fetchSize);
            ResultSet res = preparedStatement2.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\analysis_data\\" + filterValue + "\\" + filterValue + "_analysis_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitAnalysisData");
    }

    private void exportTotalCompareData(String timeStamp, int fetchSize) throws SQLException {
            String sqlFields = "SELECT * FROM(\n" +
                "SELECT \n" +
                "   tt.COLUMN_ID\n" +
                "  ,tt.column_name\n" +
                "  ,REC_SUM(tt.column_name, tt.table_name, tt.owner, 'All') sum_\n" +
                "FROM all_tab_columns tt \n" +
                "WHERE tt.table_name = upper('VT_COMPARE') and tt.COLUMN_ID > 1)\n" +
                "WHERE sum_ > 0\n" +
                "ORDER BY COLUMN_ID";

            Statement statement = conn.createStatement();
            ResultSet resBase = statement.executeQuery(sqlFields);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("DIFF");
            while (resBase.next()){
                stringBuilder.append(", ");
                stringBuilder.append(resBase.getString(2));
            }

            String fieldForQuery = stringBuilder.toString();
            if(fieldForQuery.equals("DIFF")){
                fieldForQuery = "*";
            }

            String sql = "Select " + fieldForQuery + " from VT_COMPARE ORDER BY DIFF, M_TECH_KEY";
            statement.setFetchSize(fetchSize);

            ResultSet res = statement.executeQuery(sql);

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_compare_data.csv";
            File file = new File(path);
            file.getParentFile().mkdirs();

            writeCsvFile(file, res);

        System.out.println("exportTotalCompareData");
    }
    private void exportSplitCompareData(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY, sum(DIFF) sum_ from VT_COMPARE group by SPLIT_KEY";

        HashMap<String, Integer> mapForFilter= new HashMap<>();

            getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sqlFields = "SELECT * FROM(\n" +
                    "SELECT \n" +
                    "   tt.COLUMN_ID\n" +
                    "  ,tt.column_name\n" +
                    "  ,REC_SUM(tt.column_name, tt.table_name, tt.owner, '" + filterValue + "') sum_\n" +
                    "FROM all_tab_columns tt \n" +
                    "WHERE tt.table_name = upper('VT_COMPARE') and tt.COLUMN_ID > 1)\n" +
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

            String sql = "Select " + fieldForQuery + " from VT_COMPARE where SPLIT_KEY = ? ORDER BY Diff, M_TECH_KEY";

            PreparedStatement preparedStatement2 = conn.prepareStatement(sql);
            preparedStatement2.setString(1, filterValue);
            preparedStatement2.setFetchSize(fetchSize);

            ResultSet res = preparedStatement2.executeQuery();

            String status = mapForFilter.get(filterValue) == 0 ? "passed" : "failed";

            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_compare_data_" + status + ".csv";
            File file = new File(path);

            file.getParentFile().mkdirs();

            writeCsvFile(file, res);
        }
        System.out.println("exportSplitCompareData");
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
        System.out.println("exportTotalExtraMaster");
    }
    private void exportSplitExtraMaster(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY, sum(DIFF) sum_ from VT_EXTRA_MASTER group by SPLIT_KEY";
        HashMap<String, Integer> mapForFilter= new HashMap<>();
        getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sql = "Select * from VT_EXTRA_MASTER where SPLIT_KEY = ? ORDER BY Diff, M_TECH_KEY";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setFetchSize(fetchSize);
            ResultSet res = preparedStatement.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_extra_master_data.csv";

            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitExtraMaster");
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
        System.out.println("exportTotalExtraTest");
    }
    private void exportSplitExtraTest(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY, sum(DIFF) sum_ from VT_EXTRA_TEST group by SPLIT_KEY";
        HashMap<String, Integer> mapForFilter= new HashMap<>();
        getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sql = "Select * from VT_EXTRA_TEST where SPLIT_KEY = ? ORDER BY Diff, M_TECH_KEY";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setFetchSize(fetchSize);
            ResultSet res = preparedStatement.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_extra_test_data.csv";

            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitExtraTest");
    }

    private void exportTotalDeviations(String timeStamp, int fetchSize) throws SQLException {
        String sql = "Select * from VT_COMPARE where Diff > 0 ORDER BY Diff, M_TECH_KEY";
        Statement statement = conn.createStatement();
        statement.setFetchSize(fetchSize);

        ResultSet res = statement.executeQuery(sql);

        String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_deviations_data.csv";
        File file = new File(path);
        file.getParentFile().mkdirs();

        writeCsvFile(file, res);
        System.out.println("exportTotalDeviations");
    }
    private void exportSplitDeviations(String timeStamp, int fetchSize) throws SQLException {
        String sql_base = "Select SPLIT_KEY, sum(DIFF) sum_ from VT_COMPARE where DIFF > 0 group by SPLIT_KEY";
        HashMap<String, Integer> mapForFilter= new HashMap<>();
        getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sql = "Select * from VT_COMPARE where SPLIT_KEY = ? and DIFF > 0 ORDER BY DIFF, M_TECH_KEY";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setFetchSize(fetchSize);
            ResultSet res = preparedStatement.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_deviations_data.csv";

            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitDeviations");
    }

    private void exportTotalOriginalMaster(String timeStamp, int fetchSize, String splitKey) throws SQLException {
        System.out.println(splitKey);
        String sql = "Select * from VT_REPLICA_MASTER order by " + splitKey;
        Statement statement = conn.createStatement();
        statement.setFetchSize(fetchSize);

        ResultSet res = statement.executeQuery(sql);

        String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_original_master_data.csv";
        File file = new File(path);
        file.getParentFile().mkdirs();

        writeCsvFile(file, res);
        System.out.println("exportTotalOriginalMaster");
    }
    private void exportSplitOriginalMaster(String timeStamp, int fetchSize, String splitKey) throws SQLException {
        String sql_base = "Select " + splitKey + ", sum(0) sum_ from VT_REPLICA_MASTER group by " + splitKey;
        HashMap<String, Integer> mapForFilter= new HashMap<>();
        getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sql = "Select * from VT_REPLICA_MASTER where " + splitKey + " = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setFetchSize(fetchSize);
            ResultSet res = preparedStatement.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_original_master_data.csv";

            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitOriginalMaster");
    }

    private void exportTotalOriginalTest(String timeStamp, int fetchSize, String splitKey) throws SQLException {
        String sql = "Select * from VT_REPLICA_TEST order by " + splitKey;
        Statement statement = conn.createStatement();
        statement.setFetchSize(fetchSize);

        ResultSet res = statement.executeQuery(sql);

        String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\total_original_test_data.csv";
        File file = new File(path);
        file.getParentFile().mkdirs();

        writeCsvFile(file, res);
        System.out.println("exportTotalOriginalTest");
    }
    private void exportSplitOriginalTest(String timeStamp, int fetchSize, String splitKey) throws SQLException {
        String sql_base = "Select " + splitKey + ", sum(0) sum_ from VT_REPLICA_TEST group by " + splitKey;
        HashMap<String, Integer> mapForFilter= new HashMap<>();
        getUniqueDataForFilter(sql_base, mapForFilter);

        for (String filterValue : mapForFilter.keySet()) {
            String sql = "Select * from VT_REPLICA_TEST where " + splitKey + " = ? ";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, filterValue);
            preparedStatement.setFetchSize(fetchSize);
            ResultSet res = preparedStatement.executeQuery();
            String path = System.getProperty("user.dir") + "\\export\\" + timeStamp + "\\compare_data\\" + filterValue + "\\" + filterValue + "_original_test_data.csv";

            File file = new File(path);
            file.getParentFile().mkdirs();
            writeCsvFile(file, res);
        }
        System.out.println("exportSplitOriginalTest");
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
