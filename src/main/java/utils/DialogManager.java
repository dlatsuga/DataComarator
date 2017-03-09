package utils;


import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import java.io.PrintWriter;
import java.io.StringWriter;

public class DialogManager {
    private Alert alert;


    public void showInfoDialog(String title, String text){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(text);
        alert.setHeaderText("");
        alert.showAndWait();
    }

    public void closeInfoDialog(){
        alert.hide();
    }

    public void showErrorDialog(Exception ex){

        System.out.println("ShowErrorDialog " + Thread.currentThread().getName());

        alert = new Alert(Alert.AlertType.INFORMATION);
        System.out.println(ex.getClass().getSimpleName());


// Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace :");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
// Get the Stage.
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
// Add a custom icon.
//        stage.getIcons().add(new Image(Thread.currentThread()
//                .getContextClassLoader()
//                .getResource("icon/Exclamation.png").toString()));
        stage.getIcons().add(new Image(Thread.currentThread()
                .getContextClassLoader()
                .getResource("icon/Exclamation.png").toString()));

        alert.setTitle(ex.getClass().getSimpleName());
        alert.setHeaderText("Error: " + ex.getClass().getSimpleName());
        alert.setContentText(ex.getMessage());


        alert.showAndWait();
    }



}
