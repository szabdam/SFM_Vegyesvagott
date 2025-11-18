package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.App;

import java.io.IOException;

public class MainController {
    @FXML
    private void initialize() {

    }

    public void handleFeladasButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CsomagFeladas.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void handleAtvetelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CsomagAtvetel.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }


    public void handleAdminButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("login.fxml"));
        Parent root = loader.load();

        // controller lekérése
        LoginController loginController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Bejelentkezés");
        stage.initModality(Modality.APPLICATION_MODAL); // blokkolja az alatta lévő ablakot
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait(); // megvárjuk, míg bezárják

        // Itt tudod ellenőrizni, sikeres volt-e a login
        if (loginController.isLoginSuccessful()) {
            System.out.println("Sikeres bejelentkezés – mehet az admin felület!");
            // pl. itt hívhatsz App.switchScene("admin_main.fxml")-t, ha van ilyen
        } else {
            System.out.println("Bejelentkezés megszakítva vagy sikertelen.");
        }
    }
}

