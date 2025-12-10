package org.example.frontend;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.service.AutomataService;
import org.example.service.CsomagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsomagModositasController {

    @FXML private TextField tfFelado;
    @FXML private TextField tfCimzett;
    @FXML private ComboBox<String> cbAutomata;
    @FXML private ComboBox<String> cbAllapot;
    @FXML private Button btnMentes;
    @FXML private Button btnMegse;
    @FXML private Button btnTorles;

    private final CsomagService csomagService;
    private final AutomataService automataService;

    private Csomag csomag;

    @Autowired
    public CsomagModositasController(CsomagService csomagService, AutomataService automataService) {
        this.csomagService = csomagService;
        this.automataService = automataService;
    }

    @FXML
    private void initialize() {
        // Töltsük fel az automata listát
        List<Csomagautomata> automatak = automataService.getAllAutomatak();
        cbAutomata.getItems().clear();
        for (Csomagautomata a : automatak) cbAutomata.getItems().add(a.getCim());

        // Állapotok
        cbAllapot.getItems().setAll(org.example.model.Csomag.allapotok);

        // Validáció
        btnMentes.disableProperty().bind(
                tfFelado.textProperty().isEmpty()
                        .or(tfCimzett.textProperty().isEmpty())
                        .or(cbAutomata.getSelectionModel().selectedItemProperty().isNull())
                        .or(cbAllapot.getSelectionModel().selectedItemProperty().isNull())
        );
    }

    public void setCsomag(Csomag csomag) {
        this.csomag = csomag;
        if (csomag == null) return;
        tfFelado.setText(csomag.getFelado());
        tfCimzett.setText(csomag.getCimzett());
        if (csomag.getCelautomata() != null) cbAutomata.getSelectionModel().select(csomag.getCelautomata());
        if (csomag.getAllapot() != null) cbAllapot.getSelectionModel().select(csomag.getAllapot());
    }

    @FXML
    private void handleMentes() {
        if (csomag == null) return;
        try {
            csomagService.updateCsomagAdmin(
                    csomag.getHexId(),
                    tfFelado.getText().trim(),
                    tfCimzett.getText().trim(),
                    cbAutomata.getSelectionModel().getSelectedItem(),
                    cbAllapot.getSelectionModel().getSelectedItem()
            );
            close();
        } catch (Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Mentés sikertelen: " + e.getMessage());
            a.showAndWait();
        }
    }

    @FXML
    private void handleTorles() {
        if (csomag == null) return;
        Alert conf = new Alert(Alert.AlertType.CONFIRMATION);
        conf.setTitle("Megerősítés");
        conf.setHeaderText("Csomag törlése");
        conf.setContentText("Biztosan törlöd a csomagot? A kapcsolódó rekesz felszabadul.");
        conf.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        var res = conf.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.OK) {
            try {
                csomagService.deleteCsomag(csomag.getHexId());
                close();
            } catch (Exception e) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Törlés sikertelen: " + e.getMessage());
                a.showAndWait();
            }
        }
    }

    @FXML
    private void handleMegse() {
        close();
    }

    private void close() {
        Stage stage = (Stage) btnMegse.getScene().getWindow();
        stage.close();
    }
}
