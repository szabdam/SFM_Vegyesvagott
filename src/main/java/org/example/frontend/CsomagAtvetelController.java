package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.service.CsomagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javafx.scene.text.Text;


import java.io.IOException;

@Component
public class CsomagAtvetelController {

    @FXML
    private Text txtValasz;

    @FXML
    private TextField tfPackageID;

    private final CsomagService csomagService;

    @Autowired
    public CsomagAtvetelController(CsomagService csomagService) {
        this.csomagService = csomagService;
    }


    @FXML
    private void initialize() {}

    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/main.fxml"));

        // Spring vezérelje a controllert
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void HandleEnter(ActionEvent actionEvent) {
        String id = tfPackageID.getText().trim();

        if (id.isEmpty()) {
            txtValasz.setText("Kérjük adjon meg egy csomagazonosítót!");
            return;
        }

        if (csomagService.isValidID(id)) {
            String automata = csomagService.getAutomataNev(id);
            txtValasz.setText("A csomag az alábbi automatában található:\n" + automata);
        } else {
            txtValasz.setText("Nincs ilyen csomag az adatbázisban.");
        }

        System.out.println("IdFromTf: " + id);
    }
}
