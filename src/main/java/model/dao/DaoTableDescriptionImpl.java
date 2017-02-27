package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.domain.TableDescription;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DaoTableDescriptionImpl implements DaoTableDescription {
    private Connection conn;
    private List<TableDescription> listOfTables = new ArrayList<>();
    private Set<String> setOfTableKey = new HashSet<>();

    public DaoTableDescriptionImpl() {
    }


    public List<TableDescription> getListOfTables() {
        return listOfTables;
    }
    public void setListOfTables(List<TableDescription> listOfTables) {
        this.listOfTables = listOfTables;
    }
    public Set<String> getSetOfTableKey() {
        return setOfTableKey;
    }
    public void setSetOfTableKey(Set<String> setOfTableKey) {
        this.setOfTableKey = setOfTableKey;
    }

    public DaoTableDescriptionImpl(Connection conn) {
        this.conn = conn;
    }

    public TableDescription findTableDescriptionByName(String schema, String tableName) {
        return null;
    }

    public List<TableDescription> findAllTablesDescription() {
        String sql =
                "  Select \n" +
                        "    cc.owner || cc.table_name as table_key\n" +
                        "    ,cc.column_name\n" +
                        "    ,cc.data_type\n" +
//                "    ,case \n" +
//                "      when cc.data_type = 'VARCHAR2' then\n" +
//                "      TESTIMMD.REC_COUNT(cc.column_name,cc.table_name,cc.owner)\n" +
//                "      else 0\n" +
//                "    end as cnt\n" +
                        "   ,777 as cnt\n" +
                        "  from all_tab_columns cc where cc.owner in ('TESTIMMD','VT_TRN') and cc.table_name like 'VT_%'";
        try {
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            TableDescription tableDescription = null;

            while(resultSet.next()){
                tableDescription = new TableDescription(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4));
                listOfTables.add(tableDescription);
                setOfTableKey.add(resultSet.getString(1));
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
