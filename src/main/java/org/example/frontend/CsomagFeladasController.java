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
import java.util.stream.Collectors;

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

    private CsomagService csomagService;
    private final AutomataService automataService;

    @Autowired
    public CsomagFeladasController(CsomagService csomagService,
                                   AutomataService automataService) {
        this.csomagService = csomagService;
        this.automataService = automataService;
    }

    @FXML
    private void initialize() {
        // Initialize with empty automation list - wait for size selection
        cbAutomata.getItems().clear();
        cbAutomata.setPromptText("Válassz méretet először");

        // Set up proper action handlers for the size buttons
        // Remove any existing conflicting handlers
    }

    @FXML
    private void handleSizeS() {
        setSelectedSize("S");
        updateAutomataListForSize("S");
    }

    @FXML
    private void handleSizeM() {
        setSelectedSize("M");
        updateAutomataListForSize("M");
    }

    @FXML
    private void handleSizeL() {
        setSelectedSize("L");
        updateAutomataListForSize("L");
    }

    private void setSelectedSize(String size) {
        selectedSize = size;
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

    private void updateAutomataListForSize(String meret) {
        if (cbAutomata == null) {
            System.err.println("cbAutomata is null - check FXML injection");
            return;
        }

        // Clear current automation items
        cbAutomata.getItems().clear();

        // Get all automations
        List<Csomagautomata> allAutomatak = automataService.getAllAutomatak();

        // Filter automations that have free compartments of the selected size
        List<Csomagautomata> compatibleAutomatak = allAutomatak.stream()
                .filter(automata -> hasFreeRekeszForSize(automata, meret))
                .collect(Collectors.toList());

        // Add compatible automations to the dropdown
        compatibleAutomatak.forEach(automata ->
                cbAutomata.getItems().add(automata.getCim())
        );

        // If there are compatible automations, select the first one
        if (!compatibleAutomatak.isEmpty()) {
            cbAutomata.getSelectionModel().select(0);
        } else {
            // Show message if no compatible automations found
            cbAutomata.setPromptText("Nincs megfelelő automata (" + meret + " méret)");
        }
    }

    private boolean hasFreeRekeszForSize(Csomagautomata automata, String meret) {
        return automata.getRekeszek().stream()
                .anyMatch(rekesz ->
                        !rekesz.isFoglalt() &&
                                rekesz.getMeret().equalsIgnoreCase(meret)
                );
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

    // Remove or modify the setService method to not automatically populate automations
    public void setService(CsomagService service) {
        this.csomagService = service;
        // Don't populate automations here - wait for size selection
        cbAutomata.getItems().clear();
        cbAutomata.setPromptText("Válassz méretet");
    }
}


