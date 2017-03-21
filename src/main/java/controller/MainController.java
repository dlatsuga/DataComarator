package controller;

import exceptions.ConnectionRefusedException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.geometry.Pos;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.domain.*;
import org.controlsfx.control.textfield.CustomTextField;
import org.controlsfx.control.textfield.TextFields;
import service.MainService;

import org.controlsfx.control.ListSelectionView;
import service.PatternService;
import service.ProcedureService;
import utils.DataBaseComparatorConfigManager;
import utils.DialogManager;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Collections;

public class MainController {

    private Stage mainStage;
    private MainService mainService = null;
    private PatternService patternService = new PatternService();
    private ProcedureService procedureService = new ProcedureService();
    private DialogManager dialogManager = new DialogManager();
    private DataBaseComparatorConfig dataBaseComparatorConfig = null;

    private FXMLLoader fxmlLoader;
    private Parent fxmlKeySelector;
    private KeySelectorController keySelectorController;
    private Stage keySelectorStage;

    private DataBaseTable selectedTable;
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
    private Label lbl_Group_Key;
    @FXML
    private Label lbl_Split_Key;

    @FXML
    private CustomTextField txt_Host;
    @FXML
    private CustomTextField txt_Port;
    @FXML
    private CustomTextField txt_Sid;
    @FXML
    private CustomTextField txt_User;
    @FXML
    private CustomTextField txt_DB_Link;
    @FXML
    private CustomTextField txt_Pattern_Name;

    @FXML
    private PasswordField txt_Pwd;

    @FXML
    private Button btn_Test_Conn;
    @FXML
    private Button btn_Load_Data;
    @FXML
    private Button btn_Save_Pattern;
    @FXML
    private Button btn_Execute;
    @FXML
    private Button btn_Key;
    @FXML
    private Button btn_RN_List;
    @FXML
    private Button btn_RN_Sort;
    @FXML
    private Button btn_Compare_Fields;
    @FXML
    private Button btn_Initial_Fields;
    @FXML
    private Button btn_Group_Key;
    @FXML
    private Button btn_Split_Key;

    @FXML
    private ChoiceBox<String> chb_Patterns_List;

    @FXML
    private CheckBox cb_Create_Base_Tables;
    @FXML
    private CheckBox cb_Update_RN;
    @FXML
    private CheckBox cb_Create_Res_Tables;
    @FXML
    private CheckBox cb_Extract_Data;

    @FXML
    private CheckBox cb_RecordCnt;

    @FXML
    private Pane pane_connection;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    public void initialize() throws ConnectionRefusedException {

        columnSchema.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("schema"));
        columnObjectName.setCellValueFactory(new PropertyValueFactory<DataBaseTable, String>("name"));
        columnObjectSize.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("size"));
        columnFieldsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("fieldsCount"));
        columnRowsCnt.setCellValueFactory(new PropertyValueFactory<DataBaseTable, Integer>("rowsCount"));

        columnFieldName.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("fieldName"));
        columnFieldType.setCellValueFactory(new PropertyValueFactory<TableDescription, String>("fieldType"));
        columnRecordCnt.setCellValueFactory(new PropertyValueFactory<TableDescription, Integer>("recordsCount"));

        setupClearButtonField(txt_Host);
        setupClearButtonField(txt_Port);
        setupClearButtonField(txt_Sid);
        setupClearButtonField(txt_User);
        setupClearButtonField(txt_DB_Link);
        setupClearButtonField(txt_Pattern_Name);

        initListeners();
        loadKeyPattern();

        dataBaseComparatorConfig = DataBaseComparatorConfigManager.loadConfigurationParameters();

        if (dataBaseComparatorConfig != null){
            txt_Host.setText(dataBaseComparatorConfig.getHostConfig());
            txt_Port.setText(dataBaseComparatorConfig.getPortConfig());
            txt_Sid.setText(dataBaseComparatorConfig.getSidConfig());
            txt_User.setText(dataBaseComparatorConfig.getUserConfig());
        }
     }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    private void initListeners() {
        tableDBObjects.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (tableDBObjects.getSelectionModel().getSelectedItem() != null
                && tableDBObjects.getSelectionModel().getSelectedItem() instanceof DataBaseTable) {

                selectedTable = (DataBaseTable) newValue;
                selectedTableDescriptionObservableList = (ObservableList<TableDescription>) mainService.getHashMapOfTableDesc().get(selectedTable.getObjectKey());
                createListOfFields(selectedTableDescriptionObservableList);
                tableTableDescription.setItems(selectedTableDescriptionObservableList);

                setDefaultLabels();
                enableCheckBox(selectedTable);
                btn_Execute.setDisable(false);
            }
        });

        chb_Patterns_List.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                        if(newValue == null){
                            newValue = oldValue;
                            chb_Patterns_List.setValue(oldValue);
                            txt_Pattern_Name.clear();
                        }
                        setSelectedPattern(newValue);
                    }
                }
        );
    }




    /*Валидацию полей повесить тут...*/

    /**
     * Set value for Keys according to Selected pattern*/
    private void setSelectedPattern(String patternName){
        KeyPattern selectedKeyPattern =  patternService.getPatternInstanceByName(patternName);

        lbl_Key.setText(selectedKeyPattern.getKey_for_join());
        lbl_RN_List.setText(selectedKeyPattern.getRow_number_list());
        lbl_RN_Sort.setText(selectedKeyPattern.getRow_number_sort());
        lbl_Compare_Fields.setText(selectedKeyPattern.getCompare_fields());
        lbl_Initial_Fields.setText(selectedKeyPattern.getInitial_fields());
        lbl_Group_Key.setText(selectedKeyPattern.getGroup_fields());
        lbl_Split_Key.setText(selectedKeyPattern.getExport_split_key());
    }



    private void loadKeyPattern(){
        patternService.loadKeyPatterns();
//        chb_Patterns_List.setItems(FXCollections.observableArrayList("First", "Second", "Third"));
        chb_Patterns_List.setItems(FXCollections.observableArrayList(patternService.getPatternsName()));
    }

    private void setDefaultLabels(){
        lbl_Key.setText("...");
        lbl_RN_List.setText("...");
        lbl_RN_Sort.setText("...");
        lbl_Compare_Fields.setText("All");
        lbl_Initial_Fields.setText("...");
        lbl_Group_Key.setText("...");
        lbl_Split_Key.setText("...");
    }

    private void enableCheckBox(DataBaseTable selectedTable){
        if(selectedTable.getTableType() == TableType.BASE){
            setDefaultValueForCheckBox();
                cb_Create_Base_Tables.setDisable(false);
                cb_Update_RN.setDisable(false);
                cb_Create_Res_Tables.setDisable(false);
                cb_Extract_Data.setDisable(false);
                    cb_Create_Base_Tables.setSelected(true);
                    cb_Update_RN.setSelected(false);
                    cb_Create_Res_Tables.setSelected(false);
        }
        if(selectedTable.getTableType() == TableType.REPLICA){
            setDefaultValueForCheckBox();
                cb_Create_Base_Tables.setDisable(true);
                cb_Update_RN.setDisable(false);
                cb_Create_Res_Tables.setDisable(false);
                cb_Extract_Data.setDisable(false);
                    cb_Update_RN.setSelected(true);
        }
        if(selectedTable.getTableType() == TableType.EXTRA || selectedTable.getTableType() == TableType.ANALYSIS || selectedTable.getTableType() == TableType.COMPARE){
            setDefaultValueForCheckBox();
                cb_Create_Base_Tables.setDisable(true);
                cb_Update_RN.setDisable(true);
                cb_Create_Res_Tables.setDisable(true);
                cb_Extract_Data.setDisable(false);
                    cb_Extract_Data.setSelected(true);
        }

    }

    private void setDefaultValueForCheckBox(){
        cb_Create_Base_Tables.setSelected(false);
        cb_Update_RN.setSelected(false);
        cb_Create_Res_Tables.setSelected(false);
        cb_Extract_Data.setSelected(false);
    }

    private void disableCheckBox(){
        cb_Create_Base_Tables.setDisable(true);
        cb_Update_RN.setDisable(true);
        cb_Create_Res_Tables.setDisable(true);
        cb_Extract_Data.setDisable(true);
    }

    private void enableButtons(){
        btn_Key.setDisable(false);
        btn_RN_List.setDisable(false);
        btn_RN_Sort.setDisable(false);
        btn_Compare_Fields.setDisable(false);
        btn_Initial_Fields.setDisable(false);
        btn_Group_Key.setDisable(false);
        btn_Split_Key.setDisable(false);

        btn_Test_Conn.setDisable(false);
        btn_Load_Data.setDisable(false);
    }

    private void disableButtons(){
        btn_Key.setDisable(true);
        btn_RN_List.setDisable(true);
        btn_RN_Sort.setDisable(true);
        btn_Compare_Fields.setDisable(true);
        btn_Initial_Fields.setDisable(true);
        btn_Group_Key.setDisable(true);
        btn_Split_Key.setDisable(true);

        btn_Execute.setDisable(true);

        btn_Test_Conn.setDisable(true);
        btn_Load_Data.setDisable(true);
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
        System.out.println("Inside initLoader " + Thread.currentThread().getName());
        if(fxmlLoader == null){
            try {
                fxmlLoader = new FXMLLoader(getClass().getResource("/view/selector_form.fxml"));
                fxmlKeySelector = (Parent) fxmlLoader.load();
                keySelectorController = fxmlLoader.getController();
            } catch (IOException e) {
                System.out.println("IOException " + Thread.currentThread().getName());
                e.printStackTrace();
            }
        }
    }

    public void actionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();
        // если нажата не кнопка - выходим из метода
        if (!(source instanceof Button)) {return;}
        Button clickedButton = (Button) source;
        switch (clickedButton.getId()) {
            case "btn_Test_Conn":
                System.out.println("Test connection " + Thread.currentThread().getName());

                disableButtons();
                disableCheckBox();

                progressIndicator.setVisible(true);

                Task<Boolean> taskTestConnection = new Task<Boolean>() {
                    @Override
                    protected Boolean call() throws Exception {
                        mainService = new MainService();
                        return mainService.testConnection(txt_Host.getText(), txt_Port.getText(), txt_Sid.getText(), txt_User.getText(), txt_Pwd.getText(), dataBaseComparatorConfig.getConnectionTypeConfig());
                    }
                };
                taskTestConnection.setOnRunning(event -> {
                    progressIndicator.setVisible(true);
                    tableDBObjects.setItems(null);
                    tableTableDescription.setItems(null);
                });
                taskTestConnection.setOnSucceeded(event -> {
                    System.out.println("setOnSucceeded " + Thread.currentThread().getName());
                        btn_Test_Conn.setDisable(false);
                        btn_Load_Data.setDisable(false);
                        pane_connection.getStyleClass().clear();
                        pane_connection.getStyleClass().add("subMenu");
                        progressIndicator.setVisible(false);
                });
                taskTestConnection.setOnFailed(event -> {
                    System.out.println("setOnFailed " + Thread.currentThread().getName());
                        btn_Test_Conn.setDisable(false);
                        pane_connection.getStyleClass().clear();
                        pane_connection.getStyleClass().add("rejected");
                        progressIndicator.setVisible(false);
                        dialogManager.showErrorDialog((Exception) taskTestConnection.getException());
                });

                new Thread(taskTestConnection).start();

                break;
            case "btn_Load_Data":
                System.out.println("Start Load Data " + Thread.currentThread().getName());
                disableButtons();
                setDefaultValueForCheckBox();
                disableCheckBox();

                Task<ObservableList<DataBaseTable>> taskLoadData = new Task<ObservableList<DataBaseTable>>() {
                    @Override
                    protected ObservableList<DataBaseTable> call() throws Exception {
                        System.out.println("Start convertListToHashMap " + Thread.currentThread().getName());
                        mainService.convertListToHashMap(cb_RecordCnt.isSelected(), txt_Host.getText(), txt_Port.getText(), txt_Sid.getText(), txt_User.getText(), txt_Pwd.getText(), dataBaseComparatorConfig.getConnectionTypeConfig());
                        return mainService.getTableListForView();
                    }
                };
                taskLoadData.setOnRunning(event -> {
                    System.out.println("setOnRunning convertListToHashMap " + Thread.currentThread().getName());
                    progressIndicator.setVisible(true);
                    btn_Test_Conn.setDisable(true);
                    btn_Load_Data.setDisable(true);
                    tableDBObjects.setItems(null);
                    tableTableDescription.setItems(null);
                });
                taskLoadData.setOnSucceeded(event -> {
                    System.out.println("setOnSucceeded convertListToHashMap " + Thread.currentThread().getName());
                    tableDBObjects.setItems(taskLoadData.getValue());
                    initLoader();
                    enableButtons();
                    progressIndicator.setVisible(false);
                    btn_Test_Conn.setDisable(false);
                    btn_Load_Data.setDisable(false);
                });
                taskLoadData.setOnFailed(event -> {
                        System.out.println("setOnFailed convertListToHashMap " + Thread.currentThread().getName());
                        progressIndicator.setVisible(false);
                        btn_Test_Conn.setDisable(false);
                        btn_Load_Data.setDisable(false);

                        dialogManager.showErrorDialog((Exception) taskLoadData.getException());
                });

                new Thread(taskLoadData).start();

                break;
            case "btn_Save_Pattern":
                patternService.updateKeyPatternList(txt_Pattern_Name.getText()
                        ,lbl_Key.getText()
                        ,lbl_RN_List.getText()
                        ,lbl_RN_Sort.getText()
                        ,lbl_Compare_Fields.getText()
                        ,lbl_Initial_Fields.getText()
                        ,lbl_Group_Key.getText()
                        ,lbl_Split_Key.getText());
                patternService.saveKeyPatternList();
                chb_Patterns_List.setItems(FXCollections.observableArrayList(patternService.getPatternsName()));
                txt_Pattern_Name.clear();
                break;
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
            case "btn_Group_Key":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_Group_Key.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_Split_Key":
                selectionView.getSourceItems().setAll(listOfSelectedFields);
                showDialog();
                lbl_Split_Key.setText(generateKey(selectionView.getTargetItems()));
                selectionView.getTargetItems().clear();
                break;
            case "btn_Execute":

                disableButtons();

                boolean[] checkBoxArray = new boolean[]{cb_Create_Base_Tables.isSelected()
                                                       ,cb_Update_RN.isSelected()
                                                       ,cb_Create_Res_Tables.isSelected()
                                                       ,cb_Extract_Data.isSelected()};
                String[] keysValueArray = new String[]{lbl_Key.getText()
                                                      ,lbl_RN_List.getText()
                                                      ,lbl_RN_Sort.getText()
                                                      ,lbl_Compare_Fields.getText()
                                                      ,lbl_Initial_Fields.getText()
                                                      ,lbl_Group_Key.getText()
                                                      ,lbl_Split_Key.getText()};
                Task<String> taskExecuteProcedure = new Task<String>() {
                    @Override
                    protected String call() throws Exception {
                        return procedureService.executeProcedure(checkBoxArray, keysValueArray, selectedTable.getSchema(), selectedTable.getName(), dataBaseComparatorConfig);
                    }
                };
                taskExecuteProcedure.setOnRunning(event -> progressIndicator.setVisible(true));
                taskExecuteProcedure.setOnSucceeded(event -> {
                    enableButtons();
                    btn_Execute.setDisable(false);
                    progressIndicator.setVisible(false);
                    System.out.println(taskExecuteProcedure.getValue());
                });
                taskExecuteProcedure.setOnFailed(event -> {
                    enableButtons();
                    btn_Execute.setDisable(false);
                    progressIndicator.setVisible(false);
                    dialogManager.showErrorDialog((Exception) taskExecuteProcedure.getException());
                });
                new Thread(taskExecuteProcedure).start();
                break;
        }
    }

    private void showDialog() {

        if (keySelectorStage == null) {
            keySelectorStage = new Stage();
            keySelectorStage.setTitle("Select Fields");
            keySelectorStage.setMinHeight(600);
            keySelectorStage.setMinWidth(500);
            keySelectorStage.setResizable(false);
            keySelectorStage.setScene(new Scene(fxmlKeySelector));
            selectionView.setMinHeight(550);
            selectionView.setMinWidth(450);
            ((Label) selectionView.getSourceHeader()).setText("Available");
            ((Label) selectionView.getTargetHeader()).setText("Selected");
            keySelectorController.getKeySelectorGridPane().add(selectionView, 0, 0);
            keySelectorController.getKeySelectorGridPane().setAlignment(Pos.CENTER);

            keySelectorStage.initModality(Modality.WINDOW_MODAL);
            keySelectorStage.initOwner(mainStage);
        }

        keySelectorStage.showAndWait();

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
