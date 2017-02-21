package controller;

import exceptions.ConnectionRefusedException;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.domain.DataBaseTable;
import model.domain.TableDescription;
import service.MainService;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController {

    private MainService mainService = new MainService();
    private Stage mainStage;

    @FXML
    private TableView tableDBObjects;
    @FXML
    private TableView tableTableDescription;

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
    private TableColumn<TableDescription, String> columnFieldName;
    @FXML
    private TableColumn<TableDescription, String> columnFieldType;
    @FXML
    private TableColumn<TableDescription, Integer> columnRecordCnt;

    @FXML
    private Label lblChoosenTable;

    @FXML
    public void initialize() throws ConnectionRefusedException {
        columnSchema.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("schema"));
        columnObjectName.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("name"));
        columnObjectSize.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("size"));
        columnFieldsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("fieldsCount"));
        columnRowsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("rowsCount"));

        tableDBObjects.setItems(mainService.getTableListForView());

        columnFieldName.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("fieldName"));
        columnFieldType.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("fieldType"));
        columnRecordCnt.setCellValueFactory(new PropertyValueFactory<TableDescription, Integer>("recordsCount"));

//        tableTableDescription.setItems(mainService.getTableDescriptionForView());


        initListeners();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void initListeners() {
//        tableDBObjects.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) -> lblChoosenTable.setText("Table " + newVal.));

        tableDBObjects.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableDBObjects.getSelectionModel().getSelectedItem() != null &&
                    tableDBObjects.getSelectionModel().getSelectedItem() instanceof DataBaseTable ) {
//                lblChoosenTable.setText(((DataBaseTable) newValue).getObjectKey());
                tableTableDescription.setItems((ObservableList<TableDescription>)mainService.getHashMapOfTableDesc().get(((DataBaseTable) newValue).getObjectKey()));
            }
        });
    }

}
