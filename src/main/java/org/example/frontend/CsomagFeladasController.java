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
import org.example.model.CsomagService;
import org.example.model.Csomagautomata;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private final CsomagService csomagService = new CsomagService(); // vagy kapja kívülről



    @FXML
    private void initialize() {
        List<String> automataNevek = new ArrayList<>();
        for(Csomagautomata cs : csomagService.getAutomatak()){
            automataNevek.add(cs.getCim());
        }

        cbAutomata.getItems().addAll(
                automataNevek
        );

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


    public void handleDone() {
        String felado = tfFelado.getText();
        String cimzett = tfCimzett.getText();
        String megjegyzes = tfMegjegyzes.getText();
        String automata = cbAutomata.getSelectionModel().getSelectedItem();

        Csomag csomag = new Csomag(felado, cimzett, megjegyzes, selectedSize, automata, "1");

        System.out.println("Csomag: " + csomag);

        csomagService.hozzaadCsomag(csomag);
    }


    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/main.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
