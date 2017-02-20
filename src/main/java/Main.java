import exceptions.ConnectionRefusedException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("view/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }


    public static void main(String[] args) throws ConnectionRefusedException {
        launch(args);

//        DaoFactory daoFactory = DaoFactory.getInstance();
//        DaoDataBaseTable daoDataBaseTable = daoFactory.getDaoDataBaseTable();

//        DataBaseTable dataBaseTable = daoDataBaseTable.findTableByName("public","pc");
//        System.out.println("Name : " + dataBaseTable);

//        List<DataBaseTable> listOfTables = daoDataBaseTable.findAllTablesOfVnT();
//
//        for (DataBaseTable table : listOfTables) {
//            System.out.println(table);
//        }
//
//        daoDataBaseTable.close();
    }
}
