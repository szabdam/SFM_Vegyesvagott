package org.example.frontend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.model.CsomagService;
import org.example.model.Csomagautomata;
import org.example.model.Rekesz;

import java.util.Arrays;
import java.util.List;

public class AdminController {

    @FXML
    private Button btnAutomatak;

    @FXML
    private Button btnCsomagok;

    @FXML
    private TextField tfKereses;

    @FXML
    private Button btnSzures;

    @FXML
    private Button btnAttekintes;

    @FXML
    private Button btnModositas;

    @FXML
    private TableView<Csomagautomata> tableAutomatak;

    @FXML
    private TableColumn<Csomagautomata, String> colCim;

    @FXML
    private TableColumn<Csomagautomata, String> colTelitettseg;

    @FXML
    private TableColumn<Csomagautomata, Integer> colSzabad;

    @FXML
    private TableColumn<Csomagautomata, String> colAllapot;

    @FXML
    private Label lblKarbantartas;

    private ObservableList<Csomagautomata> masterData = FXCollections.observableArrayList();
    private FilteredList<Csomagautomata> filteredData;
    private CsomagService csomagService = new CsomagService();

    @FXML
    private void initialize() {

        // =====================
        // 0) Adatok betöltése a service-ből
        // =====================
        masterData.setAll(csomagService.getAutomatak());

        // =====================
        // 1) Oszlopok összekötése
        // =====================
        colCim.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCim()));

        colTelitettseg.setCellValueFactory(cellData -> {
            Csomagautomata a = cellData.getValue();
            int total = a.getRekeszek().size();
            long foglalt = a.getRekeszek().stream().filter(Rekesz::isFoglalt).count();
            int percent = total == 0 ? 0 : (int) Math.round(foglalt * 100.0 / total);
            return new SimpleStringProperty(percent + " %");
        });

        colSzabad.setCellValueFactory(cellData -> {
            Csomagautomata a = cellData.getValue();
            int total = a.getRekeszek().size();
            long foglalt = a.getRekeszek().stream().filter(Rekesz::isFoglalt).count();
            int free = total - (int) foglalt;
            return new SimpleIntegerProperty(free).asObject();
        });

        colAllapot.setCellValueFactory(cellData -> {
            Csomagautomata a = cellData.getValue();
            String allapot = a.vanSzabadHely() ? "OK" : "TELT";
            return new SimpleStringProperty(allapot);
        });

        // =====================
        // 2) Szűrhető lista
        // =====================
        filteredData = new FilteredList<>(masterData, p -> true);
        tableAutomatak.setItems(filteredData);

        // =====================
        // 3) Keresőmező figyelése
        // =====================
        tfKereses.textProperty().addListener((obs, oldVal, newVal) -> {
            String filter = newVal == null ? "" : newVal.trim().toLowerCase();
            filteredData.setPredicate(a -> {
                if (filter.isEmpty()) return true;
                return a.getCim().toLowerCase().contains(filter);
            });
        });

        // =====================
        // 4) Karbantartás panel frissítése
        // =====================
        lblKarbantartas.setText("Karbantartás\nVálassz ki egy automatát a listából.");

        tableAutomatak.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                long foglalt = newSel.getRekeszek().stream().filter(Rekesz::isFoglalt).count();
                int total = newSel.getRekeszek().size();
                int free = total - (int) foglalt;

                lblKarbantartas.setText(
                        "Karbantartás\n\n" +
                                "Cím: " + newSel.getCim() + "\n" +
                                "Rekeszek száma: " + total + "\n" +
                                "Szabad rekeszek: " + free + "\n" +
                                "Van szabad hely: " + (newSel.vanSzabadHely() ? "igen" : "nem")
                );
            }
        });
    }

    // ----------------------------------------------------------------
    // Dummy adatok létrehozása – CSAK frontend teszthez
    // ----------------------------------------------------------------
    private void initDummyData() {
        // Példa rekeszméretek – ugyanaz a lista több automatához
        List<String> meretek1 = Arrays.asList("kicsi", "kicsi", "közepes", "közepes", "nagy");
        List<String> meretek2 = Arrays.asList("kicsi", "közepes", "közepes", "nagy", "nagy", "nagy");

        Csomagautomata a1 = new Csomagautomata("Debrecen, Kassai út 26.", meretek1);
        Csomagautomata a2 = new Csomagautomata("Debrecen, Piac utca 10.", meretek2);
        Csomagautomata a3 = new Csomagautomata("Budapest, Kossuth tér 1.", meretek1);

        // jelöljünk foglaltnak pár rekeszt (feltételezve, hogy Rekesz-nek van setFoglalt())
        a1.getRekeszek().get(0).setFoglalt(true);
        a1.getRekeszek().get(1).setFoglalt(true);

        a2.getRekeszek().forEach(r -> r.setFoglalt(true)); // teljesen tele → TELT

        a3.getRekeszek().get(2).setFoglalt(true);

        masterData.addAll(a1, a2, a3);
    }

    // ----------------------------------------------------------------
    // Gombkezelők – egyelőre csak log üzenetek
    // ----------------------------------------------------------------
    @FXML
    private void handleSzures() {
        System.out.println("Szűrés gomb (külön panel/ablak majd ide jöhet).");
    }

    @FXML
    private void handleAttekintes() {
        System.out.println("Áttekintés gomb.");
    }

    @FXML
    private void handleModositas() {
        Csomagautomata selected = tableAutomatak.getSelectionModel().getSelectedItem();
        if (selected != null) {
            System.out.println("Módosítás erre az automatára: " + selected.getCim());
            // TODO: itt jöhet külön FXML a módosításhoz
        } else {
            System.out.println("Nincs kiválasztott automata.");
        }
    }

    @FXML
    private void handleAutomatakMenu() {
        System.out.println("Automaták menü.");
    }

    @FXML
    private void handleCsomagokMenu() {
        System.out.println("Csomagok menü.");
    }
}