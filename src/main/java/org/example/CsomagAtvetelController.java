package org.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class CsomagAtvetelController {
    private String IdFromTf;

    @FXML
    private void initialize() {}

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void HandleEnter(ActionEvent actionEvent) {
        IdFromTf = ((TextField) actionEvent.getSource()).getText();
        System.out.println("IdFromTf: " + IdFromTf);
    }
}
