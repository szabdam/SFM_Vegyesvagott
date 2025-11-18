package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    // ide teheted pl. egy flaget, sikeres volt-e a login
    private boolean loginSuccessful = false;

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) throws IOException {
        String user = tfUsername.getText();
        String pass = pfPassword.getText();

        // Itt jönne az "igazi" ellenőrzés (adatbázis, config, stb.)
        // Most egyszerű, hardcode-olt példa:
        if ("admin".equals(user) && "admin".equals(pass)) {
            loginSuccessful = true;
            closeWindow();

            Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
            Scene scene = new Scene(root);

            // Stage lekérése a gomb eseményéből
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

            stage.setScene(scene);
            stage.show();
        } else {
            showError("Hibás felhasználónév vagy jelszó.");
        }
    }

    @FXML
    private void handleCancel() {
        loginSuccessful = false;
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) tfUsername.getScene().getWindow();
        stage.close();
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Bejelentkezési hiba");
        alert.setHeaderText("Sikertelen bejelentkezés");
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
