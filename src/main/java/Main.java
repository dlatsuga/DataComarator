import exceptions.ConnectionRefusedException;
import model.dao.DaoDataBaseTable;
import model.dao.DaoFactory;
import model.domain.DataBaseTable;

public class Main {
/*
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/com/scd/view/fxml/sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.show();
    }
*/

    public static void main(String[] args) throws ConnectionRefusedException {
        //launch(args);

        DaoFactory daoFactory = DaoFactory.getInstance();
        DaoDataBaseTable daoDataBaseTable = daoFactory.getDaoDataBaseTable();

        DataBaseTable dataBaseTable = daoDataBaseTable.findTableByName("public","pc");
        System.out.println("Name : " + dataBaseTable);
        daoDataBaseTable.close();
    }
}
