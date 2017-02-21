package service;

import exceptions.ConnectionRefusedException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


    public ObservableList<DataBaseTable> getTableListForView() throws ConnectionRefusedException {
        List<DataBaseTable> listOfTables = getDaoDataBaseTable().findAllTablesOfVnT();
        tableList = FXCollections.observableArrayList(listOfTables);
        return tableList;
    }

    private DaoDataBaseTable getDaoDataBaseTable() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoDataBaseTable = daoFactory.getDaoDataBaseTable();
    }

    public HashMap<String, ObservableList<TableDescription>> getHashMapOfTableDesc() {
        if(descriptionHashMap == null) {
            try {
                descriptionHashMap = convertListToHashMap();
            } catch (ConnectionRefusedException e) {
                e.printStackTrace();
            }
        }
       return descriptionHashMap;
    }

    private HashMap<String,ObservableList<TableDescription>> convertListToHashMap() throws ConnectionRefusedException {
        List<TableDescription> listOfDesc = getDaoTableDescription().findAllTablesDescription();
        Set<String> setOfTableKey = daoTableDescription.getSetOfTableKey();
        HashMap<String, ObservableList<TableDescription>> hashMap = new HashMap<>(40);
        for (String s : setOfTableKey) {
            List<TableDescription> result = listOfDesc.stream() 			//convert list to stream
                    .filter(line -> s. equals (line.getTableKey()))	//filters the line, equals to "mkyong"
                    .collect(Collectors.toList());
            tableDescriptionList = FXCollections.observableArrayList(result);
            hashMap.put(s, tableDescriptionList);
        }
        return hashMap;
    }

    private DaoTableDescription getDaoTableDescription() throws ConnectionRefusedException {
        DaoFactory daoFactory = DaoFactory.getInstance();
        return daoTableDescription = daoFactory.getDaoTableDescription();
    }



}
