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

import java.io.IOException;

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


    @FXML
    private void initialize() {
        cbAutomata.getItems().addAll(
                "Debrecen, Kassai út 26. - Automata #1",
                "Debrecen, Piac utca 10. - Automata #2",
                "Budapest, Kossuth tér 1. - Automata #3"
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

        System.out.println("Feladó    : " + felado);
        System.out.println("Címzett   : " + cimzett);
        System.out.println("Megjegyzés: " + megjegyzes);
        System.out.println("Méret     : " + selectedSize);
        System.out.println("Automata  : " + automata);

        // TODO: itt lehet majd csomag objektumot létrehozni, backendnek átadni stb.
    }

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }
}
