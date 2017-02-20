package service;

import exceptions.ConnectionRefusedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.dao.DaoDataBaseTable;
import model.dao.DaoFactory;
import model.domain.DataBaseTable;

import java.util.List;

public class MainService {
    DaoDataBaseTable daoDataBaseTable;

    public ObservableList<DataBaseTable> getTableListForView() throws ConnectionRefusedException {
        List<DataBaseTable> listOfTables = getDaoDataBaseTable().findAllTablesOfVnT();
        ObservableList<DataBaseTable> observableList = FXCollections.observableArrayList(listOfTables);
        return observableList;
    }

    private DaoDataBaseTable getDaoDataBaseTable() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoDataBaseTable = daoFactory.getDaoDataBaseTable();
    }


}
