package service;

import exceptions.ConnectionRefusedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import model.dao.DaoDataBaseTable;
import model.dao.DaoFactory;
import model.dao.DaoTableDescription;
import model.dao.DaoTableDescriptionImpl;
import model.domain.DataBaseTable;
import model.domain.TableDescription;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MainService {

    private DaoDataBaseTable daoDataBaseTable;
    private ObservableList<DataBaseTable> tableList;
    private DaoTableDescription daoTableDescription;
    private HashMap<String, ObservableList<TableDescription>> descriptionHashMap;
    private ObservableList<TableDescription> tableDescriptionList;
    boolean isValidConnection = false;

    public boolean isValidConnection() {
        return isValidConnection;
    }

    public ObservableList<DataBaseTable> getTableListForView() throws ConnectionRefusedException {
        List<DataBaseTable> listOfTables = getDaoDataBaseTable().findAllTablesOfVnT();
        tableList = FXCollections.observableArrayList(listOfTables);

        daoDataBaseTable.close();
        return tableList;
    }

    private DaoDataBaseTable getDaoDataBaseTable() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoDataBaseTable = daoFactory.getDaoDataBaseTable();
    }

    public HashMap<String, ObservableList<TableDescription>> getHashMapOfTableDesc() {
       return descriptionHashMap;
    }

    public void convertListToHashMap(boolean isUniqueCnt) throws ConnectionRefusedException {
        List<TableDescription> listOfDesc = getDaoTableDescription().findAllTablesDescription(isUniqueCnt);
        Set<String> setOfTableKey = daoTableDescription.getSetOfTableKey();
        HashMap<String, ObservableList<TableDescription>> hashMap = new HashMap<>(40);
        for (String s : setOfTableKey) {
            List<TableDescription> result = listOfDesc.stream() 			//convert list to stream
                    .filter(line -> s. equals (line.getTableKey()))
                    .collect(Collectors.toList());
            tableDescriptionList = FXCollections.observableArrayList(result);
            hashMap.put(s, tableDescriptionList);
        }
        descriptionHashMap = hashMap;

//        daoTableDescription.close(); -- do not close till execute -- getTableListForView()
    }

    private DaoTableDescription getDaoTableDescription() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoTableDescription = daoFactory.getDaoTableDescription();
    }


    public boolean testConnection(String host, String port, String sid, String user, String password) throws ConnectionRefusedException {
        isValidConnection = false;
        DaoFactory.createUrl(host, port, sid);
        DaoFactory.setUser(user);
        DaoFactory.setPassword(password);
        return isValidConnection = DaoFactory.testConnection();
    }

}
