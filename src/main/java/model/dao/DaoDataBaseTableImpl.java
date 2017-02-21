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
                        "Select\n" +
                        "        tt.table_schema || '_' || tt.table_name as table_key\n" +
                        "        tt.table_schema\n" +
                        "        ,tt.table_name\n" +
                        "        ,count(cc.column_name) fields_cnt\n" +
                        "from information_schema.tables tt\n" +
                        "left join information_schema.columns cc on tt.table_schema = cc.table_schema\n" +
                        "     and tt.table_name = cc.table_name\n" +
                        "where tt.table_schema = ? and tt.table_name = ?\n" +
                        "group by tt.table_schema, tt.table_name";
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
                "Select\n" +
                "        tt.table_schema || '_' || tt.table_name as table_key\n" +
                "        ,tt.table_schema\n" +
                "        ,tt.table_name\n" +
                "        ,count(cc.column_name) fields_cnt\n" +
                "from information_schema.tables tt\n" +
                "left join information_schema.columns cc on tt.table_schema = cc.table_schema\n" +
                "     and tt.table_name = cc.table_name\n" +
                "Where tt.table_schema in ('public', 'test')\n" +
                "group by tt.table_schema, tt.table_name";
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
