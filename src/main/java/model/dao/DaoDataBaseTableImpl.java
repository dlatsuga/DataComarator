package model.dao;

import model.domain.DataBaseTable;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoDataBaseTableImpl implements DaoDataBaseTable {
    Connection conn;

    public DaoDataBaseTableImpl(Connection conn) {
        this.conn = conn;
    }


    public DataBaseTable findTableByName(String schema, String tableName) {
        /*String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = ? and table_name = ?";*/
        String sql =
                        "SELECT\n" +
                        "  tt.owner || '.' || tt.object_name table_key\n" +
                        "  ,tt.owner\n" +
                        "  ,tt.object_name \n" +
                        "  ,nvl(round(ss.bytes/1024/1024),0) MB\n" +
                        "  ,atc.column_cnt fields\n" +
                        "  ,at.num_rows\n" +
                        "FROM all_objects tt\n" +
                        "  LEFT JOIN user_segments ss ON ss.TABLESPACE_NAME = tt.owner AND ss.segment_name = tt.object_name  AND ss.segment_type='TABLE' AND ss.segment_name LIKE 'VT_%'\n" +
                        "  LEFT JOIN all_tables at ON at.owner = tt.owner AND at.table_name = tt.object_name AND at.owner NOT LIKE 'SYS%' AND at.table_name LIKE 'VT_%'\n" +
                        "  LEFT JOIN (SELECT atc.owner, atc.table_name, count(atc.column_name) column_cnt\n" +
                        "            FROM all_tab_columns  atc\n" +
                        "            WHERE atc.table_name LIKE 'VT_%'\n" +
                        "            GROUP BY atc.owner,atc.table_name) atc ON atc.owner = tt.owner AND atc.table_name = tt.object_name \n" +
                        "WHERE tt.object_type = 'TABLE' AND tt.owner = ? AND tt.object_name = ?\n" +
                        "ORDER BY table_key DESC";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,schema);
            statement.setString(2,tableName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next() ;
            DataBaseTable dataBaseTable = new DataBaseTable(resultSet.getString(1)
                                                           ,resultSet.getString(2)
                                                           ,resultSet.getString(3)
                                                           ,resultSet.getInt(4)
                                                           ,resultSet.getInt(5)
                                                           ,resultSet.getInt(6));
            return dataBaseTable;
        } catch (SQLException e) {
            return null;
        }
    }


    public List<DataBaseTable> findAllTablesOfVnT() {
        List<DataBaseTable> listOfTables = new ArrayList<DataBaseTable>();
        String sql =
                "SELECT\n" +
                "  tt.owner || '.' || tt.object_name table_key\n" +
                "  ,tt.owner\n" +
                "  ,tt.object_name \n" +
                "  ,nvl(round(ss.bytes/1024/1024),0) MB\n" +
                "  ,atc.column_cnt fields\n" +
                "  ,at.num_rows\n" +
                "FROM all_objects tt\n" +
                "  LEFT JOIN user_segments ss ON ss.TABLESPACE_NAME = tt.owner AND ss.segment_name = tt.object_name  AND ss.segment_type='TABLE' AND ss.segment_name LIKE 'VT_%'\n" +
                "  LEFT JOIN all_tables at ON at.owner = tt.owner AND at.table_name = tt.object_name AND at.owner NOT LIKE 'SYS%' AND at.table_name LIKE 'VT_%'\n" +
                "  LEFT JOIN (SELECT atc.owner, atc.table_name, count(atc.column_name) column_cnt\n" +
                "            FROM all_tab_columns  atc\n" +
                "            WHERE atc.table_name LIKE 'VT_%'\n" +
                "            GROUP BY atc.owner,atc.table_name) atc ON atc.owner = tt.owner AND atc.table_name = tt.object_name \n" +
                "WHERE tt.object_type = 'TABLE' AND tt.object_name LIKE 'VT_%'\n" +
                "ORDER BY table_key DESC";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            DataBaseTable dataBaseTable = null;

            while(resultSet.next()){
                dataBaseTable = new DataBaseTable(resultSet.getString(1)
                                                 ,resultSet.getString(2)
                                                 ,resultSet.getString(3)
                                                 ,resultSet.getInt(4)
                                                 ,resultSet.getInt(5)
                                                 ,resultSet.getInt(6));
                listOfTables.add(dataBaseTable);
            }
            return listOfTables;
        } catch (SQLException e) {
            return null;
        }
    }

    public void close() {
        try {
            conn.close();
            System.out.println("Dao Data Base Table -- Close");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
