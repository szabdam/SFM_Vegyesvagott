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
import org.example.model.Csomagautomata;
import org.example.service.CsomagService;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MainController {
    @FXML
    Button btnAdmin;

    @FXML
    private void initialize() {
    }


    public void handleFeladasButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/csomagFeladas.fxml"));
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();

        // --- Itt kérjük le a controllert ---
        CsomagFeladasController controller = loader.getController();

        // --- Itt adjuk át a service-t ---
        controller.setService(org.example.Launcher.context.getBean(CsomagService.class));

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleAtvetelButton(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/csomagAtvetel.fxml"));
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleAdminButton() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/login.fxml"));
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();
        LoginController loginController = loader.getController();

        Stage loginStage = new Stage();
        loginStage.setTitle("Bejelentkezés");
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setScene(new Scene(root));
        loginStage.setResizable(false);
        loginStage.showAndWait();

        // Ha sikeres login → admin felület betöltése
        if (loginController.isLoginSuccessful()) {

            FXMLLoader adminLoader = new FXMLLoader(getClass().getResource("/org/example/admin.fxml"));
            adminLoader.setControllerFactory(org.example.Launcher.context::getBean);

            Parent adminRoot = adminLoader.load();

            Stage mainStage = (Stage) btnAdmin.getScene().getWindow();
            mainStage.setScene(new Scene(adminRoot));
            mainStage.show();
        }
    }
}
