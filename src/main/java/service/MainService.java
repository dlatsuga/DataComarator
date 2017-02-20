package service;

import exceptions.ConnectionRefusedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.DaoDataBaseTable;
import model.dao.DaoFactory;
import model.dao.DaoTableDescriptionImpl;
import model.domain.DataBaseTable;
import model.domain.TableDescription;

import java.util.List;

public class MainService {
    private DaoDataBaseTable daoDataBaseTable;
    private ObservableList<DataBaseTable> tableList;
    private DaoTableDescriptionImpl daoTableDescription = new DaoTableDescriptionImpl();

    public ObservableList<DataBaseTable> getTableListForView() throws ConnectionRefusedException {
        List<DataBaseTable> listOfTables = getDaoDataBaseTable().findAllTablesOfVnT();
        tableList = FXCollections.observableArrayList(listOfTables);
        return tableList;
    }

    private DaoDataBaseTable getDaoDataBaseTable() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoDataBaseTable = daoFactory.getDaoDataBaseTable();
    }

    public ObservableList<TableDescription> getTableDescriptionForView() {
        daoTableDescription.fillTestData();
        return daoTableDescription.getTableDescriptionList();
    }
}
