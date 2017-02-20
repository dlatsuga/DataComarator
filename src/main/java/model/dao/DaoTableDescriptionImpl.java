package model.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.domain.TableDescription;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DaoTableDescriptionImpl implements DaoTableDescription {
    private Connection conn;
    private ObservableList<TableDescription> tableDescriptionList = FXCollections.observableArrayList();

    public DaoTableDescriptionImpl() {
    }

    public DaoTableDescriptionImpl(Connection conn) {
        this.conn = conn;
    }

    public TableDescription findTableDescriptionByName(String schema, String tableName) {
        return null;
    }

    public List<TableDescription> findAllTablesDescription() {
        return null;
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillTestData(){
        tableDescriptionList.add(new TableDescription("publicpc", "Portfolio", "String", 50));
        tableDescriptionList.add(new TableDescription("publicpc", "Secshort", "String", 32));
        tableDescriptionList.add(new TableDescription("publicpc", "BalNomVal", "Integer", 100));
        tableDescriptionList.add(new TableDescription("publicpc", "AAA", "String", 20));
        tableDescriptionList.add(new TableDescription("testpc", "Portfolio", "String", 350));
        tableDescriptionList.add(new TableDescription("testpc", "Secshort", "String", 4532));
        tableDescriptionList.add(new TableDescription("testpc", "BalNomVal", "Integer", 2100));
    }

    public ObservableList<TableDescription> getTableDescriptionList() {
        return tableDescriptionList;
    }
}
