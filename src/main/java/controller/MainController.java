package controller;

import exceptions.ConnectionRefusedException;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import model.domain.DataBaseTable;
import service.MainService;

public class MainController {

    private MainService mainService = new MainService();

    @FXML
    private TableView tableDBObjects;

    @FXML
    private TableColumn<DataBaseTable, String> columnSchema;

    @FXML
    private TableColumn<DataBaseTable, String> columnObjectName;

    @FXML
    private TableColumn<DataBaseTable, Integer> columnObjectSize;

    @FXML
    private TableColumn<DataBaseTable, Integer> columnFieldsCnt;

    @FXML
    private TableColumn<DataBaseTable, Integer>  columnRowsCnt;


    @FXML
    private void initialize() throws ConnectionRefusedException {
        columnSchema.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("schema"));
        columnObjectName.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("name"));
        columnObjectSize.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("size"));
        columnFieldsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("fieldsCount"));
        columnRowsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("rowsCount"));

        tableDBObjects.setItems(mainService.getTableListForView());
    }

}
