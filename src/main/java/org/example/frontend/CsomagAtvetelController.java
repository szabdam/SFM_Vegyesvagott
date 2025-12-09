package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Csomag;
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

    @FXML
    private void HandleEnter() {

        String kod = tfPackageID.getText().trim();

        if (kod.isEmpty()) {
            txtValasz.setText("Kérjük, adja meg a csomagkódot!");
            return;
        }

        // keresés adatbázisban
        var talalat = csomagService.findByCsomagKod(kod);

        if (talalat.isEmpty()) {
            txtValasz.setText("Nincs ilyen csomag a rendszerben: " + kod);
            return;
        }

        Csomag c = talalat.get();

        txtValasz.setText(
                        "Címzett: " + c.getCimzett() + "\n" +
                        "Feladó: " + c.getFelado() + "\n" +
                        "Automata: " + c.getCelautomata()
        );
    }
}
