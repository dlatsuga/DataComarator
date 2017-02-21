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
                "Select \n" +
                "\t tt.table_schema || '_' || tt.table_name as table_key\n" +
                "    ,tt.column_name\n" +
                "    ,tt.udt_name\n" +
                "    ,public.REC_COUNT(tt.column_name,tt.table_name,tt.table_schema) as REC_COUNT\n" +
                "from information_schema.columns tt\n" +
                "where tt.table_schema in ('public','test')";
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
