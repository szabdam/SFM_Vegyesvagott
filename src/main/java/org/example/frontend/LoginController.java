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

        if ("admin".equals(user) && "admin".equals(pass)) {
            loginSuccessful = true;

            // bezárjuk a login popupot
            Stage loginStage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            loginStage.close();

            // FŐ ablakra váltjuk az admin.fxml-t
            FXMLLoader loader = new FXMLLoader(App.class.getResource("admin.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage mainStage = App.getPrimaryStage();
            mainStage.setScene(scene);
            mainStage.show();

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
