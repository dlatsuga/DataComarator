package controller;

import exceptions.ConnectionRefusedException;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.geometry.Pos;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.domain.DataBaseTable;
import model.domain.TableDescription;
import service.MainService;

import org.controlsfx.control.ListSelectionView;

import java.io.IOException;

public class MainController {

    private MainService mainService = new MainService();
    private Stage mainStage;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Parent fxmlKeySelector;
    private KeySelectorController keySelectorController;
    private Stage keySelectorStage;

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
    private TableColumn<DataBaseTable, Integer> columnRowsCnt;

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
        initLoader();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void initListeners() {
//        tableDBObjects.getSelectionModel().selectedItemProperty().addListener((changed, oldVal, newVal) -> lblChoosenTable.setText("Table " + newVal.));

        tableDBObjects.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            //Check whether item is selected and set value of selected item to Label
            if (tableDBObjects.getSelectionModel().getSelectedItem() != null &&
                    tableDBObjects.getSelectionModel().getSelectedItem() instanceof DataBaseTable) {
//                lblChoosenTable.setText(((DataBaseTable) newValue).getObjectKey());
                tableTableDescription.setItems((ObservableList<TableDescription>) mainService.getHashMapOfTableDesc().get(((DataBaseTable) newValue).getObjectKey()));
            }
        });
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../view/keyselector.fxml"));
            fxmlKeySelector = fxmlLoader.load();
            keySelectorController = fxmlLoader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void actionButtonPressed(ActionEvent actionEvent) {

        Object source = actionEvent.getSource();
        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {return;}

        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnLoadKey":
                showDialog();
                break;
        }
    }

    private void showDialog() {

        if (keySelectorStage == null) {
            keySelectorStage = new Stage();
            keySelectorStage.setTitle("Choose Key");
            keySelectorStage.setMinHeight(275);
            keySelectorStage.setMinWidth(400);
            keySelectorStage.setResizable(false);
            keySelectorStage.setScene(new Scene(fxmlKeySelector));

            ListSelectionView<String> view = new ListSelectionView<>();
            view.getSourceItems().addAll("Katja", "Dirk", "Philip", "Jule", "Armin");

            ((GridPane)keySelectorStage.getScene().getRoot()).add(view, 0, 0);
            ((GridPane)keySelectorStage.getScene().getRoot()).setAlignment(Pos.CENTER);





            keySelectorStage.initModality(Modality.WINDOW_MODAL);
            keySelectorStage.initOwner(mainStage);
        }

        keySelectorStage.showAndWait(); // для ожидания закрытия окна

    }



}
