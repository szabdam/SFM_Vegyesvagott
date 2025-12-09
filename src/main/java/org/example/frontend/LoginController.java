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
import org.example.App;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginController {

    @FXML
    private TextField tfUsername;

    @FXML
    private PasswordField pfPassword;

    private boolean loginSuccessful = false;

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    @FXML
    private void handleLogin(ActionEvent event) {

        String user = tfUsername.getText();
        String pass = pfPassword.getText();

        if (user.equals("admin") && pass.equals("admin")) {

            loginSuccessful = true;

            // popup bezárása
            Stage stage = (Stage) tfUsername.getScene().getWindow();
            stage.close();

        } else {
            showError("Hibás felhasználónév vagy jelszó!");
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
