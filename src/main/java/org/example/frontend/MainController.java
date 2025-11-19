package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;

public class MainController {
    @FXML
    Button btnAdmin;

    @FXML
    private void initialize() {}

    public void handleFeladasButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("csomagFeladas.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void handleAtvetelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("csomagAtvetel.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void handleAdminButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Bejelentkezés");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait(); // itt megvárjuk, míg bejelentkezik vagy bezárja

        if (loginController.isLoginSuccessful()) {
            // SIKERES LOGIN → itt váltunk admin.fxml-re a FŐ ablakon
            FXMLLoader adminLoader = new FXMLLoader(App.class.getResource("admin.fxml"));
            Parent adminRoot = adminLoader.load();
            Scene adminScene = new Scene(adminRoot);

            Stage primaryStage = (Stage) btnAdmin.getScene().getWindow();
            primaryStage.setScene(adminScene);
            primaryStage.show();
        } else {
            System.out.println("Sikertelen / megszakított bejelentkezés.");
        }
    }
}

