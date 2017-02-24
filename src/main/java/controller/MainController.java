package controller;

import exceptions.ConnectionRefusedException;
import javafx.beans.property.ObjectProperty;
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
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import service.MainService;

import org.controlsfx.control.ListSelectionView;

import java.io.IOException;
import java.lang.reflect.Method;

public class MainController {

    private MainService mainService = new MainService();
    private Stage mainStage;

    private FXMLLoader fxmlLoader = new FXMLLoader();
    private Parent fxmlKeySelector;
    private KeySelectorController keySelectorController;
    private Stage keySelectorStage;
    private ObservableList<TableDescription> selectedTableDescriptionObservableList;



    private ObservableList<String> listOfSelectedFields = FXCollections.observableArrayList();
    private ListSelectionView<String> selectionView = new ListSelectionView<>();


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
    private Label lbl_Key;
    @FXML
    private Label lbl_RN_List;
    @FXML
    private Label lbl_RN_Sort;
    @FXML
    private Label lbl_Compare_Fields;
    @FXML
    private Label lbl_Initial_Fields;

    @FXML
    private CustomTextField txtHost;
    @FXML
    private CustomTextField txtPort;
    @FXML
    private CustomTextField txtSID;
    @FXML
    private CustomTextField txtUser;



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

        setupClearButtonField(txtHost);
        setupClearButtonField(txtPort);
        setupClearButtonField(txtSID);
        setupClearButtonField(txtUser);
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

                setDefaultLabels();
            }
        });

//        selectionView.getTargetItems().addListener(new ListChangeListener<String>() {
//            @Override
//            public void onChanged(Change<? extends String> c) {
//                lbl_Key.setText(generateKey(selectionView.getTargetItems()));
//            }
//        });


    }

    private void setDefaultLabels(){
            lbl_Key.setText("...");
            lbl_RN_List.setText("...");
            lbl_RN_Sort.setText("...");
            lbl_Compare_Fields.setText("All");
            lbl_Initial_Fields.setText("...");
    }


    private void setupClearButtonField(CustomTextField customTextField) {
        try {
            Method m = TextFields.class.getDeclaredMethod("setupClearButtonField", TextField.class, ObjectProperty.class);
            m.setAccessible(true);
            m.invoke(null, customTextField, customTextField.rightProperty());
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private void createListOfFields(ObservableList<TableDescription> selectedTableDescriptionObservableList){
        listOfSelectedFields.clear();
        for (TableDescription tableDescription : selectedTableDescriptionObservableList) {
            listOfSelectedFields.add(tableDescription.getFieldName());
        }
    }

    private void initLoader() {
        try {
            fxmlLoader.setLocation(getClass().getResource("../view/selector_form.fxml"));
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
            case "btn_Key":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_Key.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_RN_List":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_RN_List.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_RN_Sort":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_RN_Sort.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_Compare_Fields":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_Compare_Fields.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_Initial_Fields":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_Initial_Fields.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
        }
    }

    private void showDialog() {

        if (keySelectorStage == null) {
            keySelectorStage = new Stage();
            keySelectorStage.setTitle("Select Fields");
            keySelectorStage.setMinHeight(275);
            keySelectorStage.setMinWidth(400);
            keySelectorStage.setResizable(false);
            keySelectorStage.setScene(new Scene(fxmlKeySelector));

//            view.getSourceItems().addAll("Katja", "Dirk", "Philip", "Jule", "Armin");

//            ((GridPane)keySelectorStage.getScene().getRoot()).add(view, 0, 0);
//            ((GridPane)keySelectorStage.getScene().getRoot()).setAlignment(Pos.CENTER);

            ((Label) selectionView.getSourceHeader()).setText("Available");
            ((Label) selectionView.getTargetHeader()).setText("Selected");
            keySelectorController.getKeySelectorGridPane().add(selectionView, 0, 0);
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
