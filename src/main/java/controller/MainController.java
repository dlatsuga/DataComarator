package controller;

import exceptions.ConnectionRefusedException;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.geometry.Pos;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.domain.DataBaseTable;
import model.domain.TableDescription;
import service.MainService;

import org.controlsfx.control.ListSelectionView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    private MainService mainService = new MainService();
    private Stage mainStage;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Parent fxmlKeySelector;
    private KeySelectorController keySelectorController;
    private Stage keySelectorStage;
    private ObservableList<TableDescription> selectedTableDescriptionObservableList;
    private ObservableList<String> listOfSelectedFields = FXCollections.observableArrayList();

    private ListSelectionView<String> view = new ListSelectionView<>();

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
    private Label lblTest;


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
                selectedTableDescriptionObservableList = (ObservableList<TableDescription>) mainService.getHashMapOfTableDesc().get(((DataBaseTable) newValue).getObjectKey());
                createListOfFields(selectedTableDescriptionObservableList);
                tableTableDescription.setItems(selectedTableDescriptionObservableList);
            }
        });

        view.getTargetItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(Change<? extends String> c) {
                lblTest.setText(generateKey(view.getTargetItems()));
            }
        });





    }

    private void createListOfFields(ObservableList<TableDescription> selectedTableDescriptionObservableList){
        listOfSelectedFields.clear();
        for (TableDescription tableDescription : selectedTableDescriptionObservableList) {
            listOfSelectedFields.add(tableDescription.getFieldName());
        }
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
                view.getSourceItems().setAll(listOfSelectedFields);
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

//            view.getSourceItems().addAll("Katja", "Dirk", "Philip", "Jule", "Armin");

//            ((GridPane)keySelectorStage.getScene().getRoot()).add(view, 0, 0);
//            ((GridPane)keySelectorStage.getScene().getRoot()).setAlignment(Pos.CENTER);

            ((Label)view.getSourceHeader()).setText("Available");
            ((Label)view.getTargetHeader()).setText("Selected");
            keySelectorController.getKeySelectorGridPane().add(view, 0, 0);
            keySelectorController.getKeySelectorGridPane().setAlignment(Pos.CENTER);


            keySelectorStage.initModality(Modality.WINDOW_MODAL);
            keySelectorStage.initOwner(mainStage);
        }

        keySelectorStage.showAndWait(); // для ожидания закрытия окна

    }

    public String generateKey(ObservableList<String> listOfFields){

        StringBuffer output = new StringBuffer(200);

        if(listOfFields.size() >= 1){
            output.append(listOfFields.get(0));
            for(int i =1; i < listOfFields.size(); i++) {
                output.append(",");
                output.append(listOfFields.get(i));
            }
        }

        return output.toString();
    }



}
