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
                        "Select \n" +
                        "  tt.owner || '.' || tt.object_name  as table_key\n" +
                        "  ,tt.owner\n" +
                        "  ,tt.object_name \n" +
                        "  ,count(cc.column_name) cnt_\n" +
                        "From all_objects tt\n" +
                        "     left join all_tab_columns cc on cc.owner = tt.owner and tt.object_name = cc.table_name\n" +
                        "Where tt.object_type = 'TABLE' And tt.owner = ? And tt.object_name = ? \n" +
                        "group by \n" +
                        "  tt.owner || tt.object_name\n" +
                        "  ,tt.owner\n" +
                        "  ,tt.object_name";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,schema);
            statement.setString(2,tableName);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next() ;
            DataBaseTable dataBaseTable = new DataBaseTable(resultSet.getString(1), schema, tableName, resultSet.getInt(4));
            return dataBaseTable;
        } catch (SQLException e) {
            return null;
        }
    }

    public List<DataBaseTable> findAllTablesOfVnT() {
        List<DataBaseTable> listOfTables = new ArrayList<DataBaseTable>();
        String sql =
                "Select \n" +
                        "  tt.owner || '.' || tt.object_name  as table_key\n" +
                        "  ,tt.owner\n" +
                        "  ,tt.object_name \n" +
                        "  ,count(cc.column_name) cnt_\n" +
                        "From all_objects tt\n" +
                        "     left join all_tab_columns cc on cc.owner = tt.owner and tt.object_name = cc.table_name\n" +
//                "Where tt.object_type = 'TABLE' And tt.owner = 'TESTIMMD' And tt.object_name = 'VT_DTLS_TEST'\n" +
                        "Where tt.object_type = 'TABLE' And tt.owner in ('TESTIMMD','VT_TRN') And tt.object_name like 'VT_%'\n" +
                        "group by \n" +
                        "  tt.owner || tt.object_name\n" +
                        "  ,tt.owner\n" +
                        "  ,tt.object_name";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            DataBaseTable dataBaseTable = null;

            while(resultSet.next()){
                dataBaseTable = new DataBaseTable(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
