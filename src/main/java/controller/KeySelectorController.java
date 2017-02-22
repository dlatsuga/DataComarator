package controller;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

public class KeySelectorController {
    @FXML
    private GridPane keySelectorGridPane;

    public GridPane getKeySelectorGridPane() {
        return keySelectorGridPane;
    }
    public void setKeySelectorGridPane(GridPane keySelectorGridPane) {
        this.keySelectorGridPane = keySelectorGridPane;
    }
}
