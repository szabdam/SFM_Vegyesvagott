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

    private boolean loginSuccessful = false;

    public boolean isLoginSuccessful() {
        return loginSuccessful;
    }

    @FXML
    private void handleLogin(ActionEvent actionEvent) throws IOException {
        String user = tfUsername.getText();
        String pass = pfPassword.getText();

        if (user.equals("admin") && pass.equals("admin")) {

            // close login popup
            Stage loginStage = (Stage) tfUsername.getScene().getWindow();
            loginStage.close();

            Stage primaryStage = App.getPrimaryStage();

            // --- FONTOS: NEM load(), hanem teljes FXMLLoader ---
            FXMLLoader loader = new FXMLLoader(App.class.getResource("admin.fxml"));
            Parent root = loader.load();

            // --- Controller lekérése ---
            AdminController controller = loader.getController();

            // --- KÖZÖS CSOMAG SERVICE átadása ---
            controller.setService(App.globalService);

            // --- Scene beállítása ---
            Scene adminScene = new Scene(root);
            primaryStage.setScene(adminScene);
            primaryStage.show();

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
