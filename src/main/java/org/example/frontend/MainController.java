package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    private void initialize() {

    }

    public void handleFeladasButton(ActionEvent actionEvent) {
        System.out.println("Feladas button clicked");
    }

    public void handleAtvetelButton(ActionEvent actionEvent) {
        System.out.println("Atvetel button clicked");
    }
}
