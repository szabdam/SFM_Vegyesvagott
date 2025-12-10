package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.service.AutomataService;
import org.example.service.CsomagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class CsomagFeladasController {

    @FXML
    private Button btnS;

    @FXML
    private Button btnM;

    @FXML
    private Button btnL;

    @FXML
    private ComboBox<String> cbAutomata;

    @FXML
    private TextField tfFelado;

    @FXML
    private TextField tfCimzett;

    @FXML
    private TextField tfMegjegyzes;


    private String selectedSize = null;

    private CsomagService csomagService; // vagy kapja kívülrőlú
    private final AutomataService automataService;

    @Autowired
    public CsomagFeladasController(CsomagService csomagService,
                                   AutomataService automataService) {
        this.csomagService = csomagService;
        this.automataService = automataService;
    }

    private int cnt = 0;

    @FXML
    void btnS(ActionEvent event) {}

    @FXML
    private void initialize() {
        // AUTOMATÁK BETÖLTÉSE AZ ADATBÁZISBÓL
        List<Csomagautomata> automatak = automataService.getAllAutomatak();

        cbAutomata.getItems().clear();
        for (Csomagautomata a : automatak) {
            cbAutomata.getItems().add(a.getCim());
        }

        if (!cbAutomata.getItems().isEmpty()) {
            cbAutomata.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void handleSizeS() {
        selectedSize = "S";
        System.out.println("Selected Size: " + selectedSize);
        updateSizeButtonStyles();
    }

    @FXML
    private void handleSizeM() {
        selectedSize = "M";
        System.out.println("Selected Size: " + selectedSize);
        updateSizeButtonStyles();
    }

    @FXML
    private void handleSizeL() {
        selectedSize = "L";
        System.out.println("Selected Size: " + selectedSize);
        updateSizeButtonStyles();
    }

    private void updateSizeButtonStyles() {
        resetButtonStyle(btnS);
        resetButtonStyle(btnM);
        resetButtonStyle(btnL);

        switch (selectedSize) {
            case "S" -> highlightButton(btnS);
            case "M" -> highlightButton(btnM);
            case "L" -> highlightButton(btnL);
        }
    }

    private void resetButtonStyle(Button btn) {
        btn.setStyle("");
    }

    private void highlightButton(Button btn) {
        btn.setStyle("-fx-background-color: #00997a; -fx-text-fill: white;");
    }


    @FXML
    public void handleDone(ActionEvent event) throws IOException {
        if (selectedSize == null) {
            System.out.println("Nincs méret kiválasztva.");
            return;
        }

        String felado = tfFelado.getText().trim();
        String cimzett = tfCimzett.getText().trim();
        String megjegyzes = tfMegjegyzes.getText().trim();
        String automata = cbAutomata.getSelectionModel().getSelectedItem();

        if (felado.isEmpty() || cimzett.isEmpty() || automata == null) {
            System.out.println("Hiányzó adatok!");
            return;
        }

        Csomag csomag = new Csomag(
                selectedSize,   // meret
                cimzett,
                felado,
                megjegyzes,
                automata
        );

        csomagService.saveCsomag(csomag);

        System.out.println("Csomag mentve: " + csomag);
        handleBack(event);
    }


    @FXML
    public void handleBack(ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/main.fxml"));
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void setService(CsomagService service) {
        this.csomagService = service;

        cbAutomata.getItems().clear();
        for (Csomagautomata a : automataService.getAllAutomatak()) {
            cbAutomata.getItems().add(a.getCim());
        }
    }


}
