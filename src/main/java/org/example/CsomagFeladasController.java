package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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

    // Ebben tároljuk, hogy S / M / L közül mit választott
    private String selectedSize = null;

    // ----------------------------------------------------------------
    // initialize() – automatikusan lefut, amikor az FXML betöltődik
    // ----------------------------------------------------------------
    @FXML
    private void initialize() {
        // Dummy automata lista – később jöhet adatbázisból / backendből
        cbAutomata.getItems().addAll(
                "Debrecen, Kassai út 26. - Automata #1",
                "Debrecen, Piac utca 10. - Automata #2",
                "Budapest, Kossuth tér 1. - Automata #3"
        );

        if (!cbAutomata.getItems().isEmpty()) {
            cbAutomata.getSelectionModel().selectFirst();
        }

        // opcionálisan beállíthatunk kezdeti méretet is
        // handleSizeM(null);  // pl. alapból M
    }

    // ----------------------------------------------------------------
    // Méretválasztó gombok kezelése
    // ----------------------------------------------------------------

    @FXML
    private void handleSizeS() {
        selectedSize = "S";
        updateSizeButtonStyles();
    }

    @FXML
    private void handleSizeM() {
        selectedSize = "M";
        updateSizeButtonStyles();
    }

    @FXML
    private void handleSizeL() {
        selectedSize = "L";
        updateSizeButtonStyles();
    }

    /**
     * A kiválasztott méret gombját kiemeli, a többiről leveszi a kiemelést.
     */
    private void updateSizeButtonStyles() {
        // alap stílus mindháromnak
        resetButtonStyle(btnS);
        resetButtonStyle(btnM);
        resetButtonStyle(btnL);

        // kiválasztott méret kiemelése
        switch (selectedSize) {
            case "S" -> highlightButton(btnS);
            case "M" -> highlightButton(btnM);
            case "L" -> highlightButton(btnL);
        }
    }

    private void resetButtonStyle(Button btn) {
        btn.setStyle(""); // vissza a CSS-ben definiált alap stílusra
    }

    private void highlightButton(Button btn) {
        // egyszerű példa – ezt érdemes majd CSS-ben szebben megoldani
        btn.setStyle("-fx-background-color: #2D8CFF; -fx-text-fill: white;");
    }

    // ----------------------------------------------------------------
    // Itt jöhet majd a "Feladás" logika: adatok bekérése, mentés, stb.
    // ----------------------------------------------------------------

    /**
     * Példa: egy metódus, ami összegyűjti az aktuális csomag adatait.
     * Ezt majd egy 'Feladás' gomb onAction-jéhez kötheted.
     */
    public void processShipment() {
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
}
