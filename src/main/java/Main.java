import controller.MainController;
import exceptions.ConnectionRefusedException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("view/main_form.fxml"));

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("view/main_form.fxml"));
        Parent fxmlMain = fxmlLoader.load();

        MainController mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);

        primaryStage.setTitle("V&T DataBase Comparator");
        primaryStage.setScene(new Scene(fxmlMain, 900, 800));
        primaryStage.setResizable(false);


            Image applicationMainIcon = new Image(getClass().getResourceAsStream("icon/Main.png"));
            primaryStage.getIcons().add(applicationMainIcon);

        primaryStage.show();
    }

    public static void main(String[] args) throws ConnectionRefusedException {
        launch(args);

    }
}

