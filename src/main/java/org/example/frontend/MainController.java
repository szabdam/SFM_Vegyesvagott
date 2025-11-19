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
    private void initialize() {
    }

    public void handleFeladasButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/csomagFeladas.fxml"));
        Parent root = loader.load();

        CsomagFeladasController feladasController = loader.getController();
        feladasController.setService(App.globalService);   // <-- ITT adjuk át

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleAtvetelButton(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/csomagAtvetel.fxml"));
        Parent root = loader.load();

        CsomagAtvetelController atvetelController = loader.getController();
        atvetelController.setService(App.globalService);  // <<< FONTOS

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }


    @FXML
    private void handleAdminButton() throws IOException {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/org/example/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();

        Stage stage = new Stage();
        stage.setTitle("Bejelentkezés");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.showAndWait(); // itt megvárjuk, míg bejelentkezik vagy bezárja

        if (loginController.isLoginSuccessful()) {
            FXMLLoader adminLoader = new FXMLLoader(App.class.getResource("/org/example/admin.fxml"));
            Parent adminRoot = adminLoader.load();

            // <-- ITT adjuk át a service-t az AdminControllernek
            AdminController adminController = adminLoader.getController();
            adminController.setService(App.globalService);

            Stage primaryStage = (Stage) btnAdmin.getScene().getWindow();
            primaryStage.setScene(new Scene(adminRoot));
            primaryStage.show();
        } else {
            System.out.println("Sikertelen / megszakított bejelentkezés.");
        }
    }
}
