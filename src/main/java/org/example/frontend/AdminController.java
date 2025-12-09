package org.example.frontend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.example.service.AutomataService;
import org.example.service.CsomagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdminController {

    @Autowired
    private CsomagService csomagService;

    @Autowired
    private AutomataService automataService;

    // Bal oldali menü gombjai
    @FXML
    private Button btnAutomatak;

    @FXML
    private Button btnCsomagok;

    // Kereső
    @FXML
    private TextField tfKereses;

    // Felső gombok
    @FXML
    private Button btnSzures;

    @FXML
    private Button btnAttekintes;

    @FXML
    private Button btnModositas;

    // ------------ AUTOMATÁK TÁBLÁZAT ------------
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

    // ------------ CSOMAGOK TÁBLÁZAT ------------
    @FXML
    private TableView<Csomag> tableCsomagok;

    @FXML
    private TableColumn<Csomag, String> colFelado;

    @FXML
    private TableColumn<Csomag, String> colCimzett;

    @FXML
    private TableColumn<Csomag, String> colCelautomata;

    // Jobb oldali panel
    @FXML
    private Label lblKarbantartas;

    // --- KÖZÖS SERVICE (NEM példányosítjuk itt!) ---
    //private CsomagService csomagService;

    // Listák
    private ObservableList<Csomagautomata> masterAutomataData = FXCollections.observableArrayList();
    private ObservableList<Csomag> masterCsomagData = FXCollections.observableArrayList();

    private FilteredList<Csomagautomata> filteredAutomatak;
    private FilteredList<Csomag> filteredCsomagok;

    // =====================================================
    //                    INITIALIZE()
    // =====================================================
    @FXML
    private void initialize() {

        // 1) Automaták oszlopok
        colCim.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCim()));

        colTelitettseg.setCellValueFactory(cell -> {
            Csomagautomata a = cell.getValue();
            int total = a.getRekeszek().size();
            long foglalt = a.getRekeszek().stream().filter(Rekesz::isFoglalt).count();
            int percent = total == 0 ? 0 : (int) Math.round(foglalt * 100.0 / total);
            return new SimpleStringProperty(percent + " %");
        });

        colSzabad.setCellValueFactory(cell -> {
            Csomagautomata a = cell.getValue();
            int total = a.getRekeszek().size();
            long foglalt = a.getRekeszek().stream().filter(Rekesz::isFoglalt).count();
            int free = total - (int) foglalt;
            return new SimpleIntegerProperty(free).asObject();
        });

        colAllapot.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().vanSzabadHely() ? "OK" : "TELT"));

        // 2) Csomagok oszlopok
        colFelado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFelado()));

        colCimzett.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCimzett()));

        colCelautomata.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCelautomata()));

        // --- Adatok betöltése JPA-ból ---
        masterAutomataData.setAll(automataService.getAllAutomatak());
        masterCsomagData.setAll(csomagService.getAllCsomagok());

        // 3) Filterelt listák (egyelőre üres master listákon)
        filteredAutomatak = new FilteredList<>(masterAutomataData, p -> true);
        filteredCsomagok = new FilteredList<>(masterCsomagData, p -> true);

        tableAutomatak.setItems(filteredAutomatak);
        tableCsomagok.setItems(filteredCsomagok);

        // 4) Kereső: mindkét listát szűri
        tfKereses.textProperty().addListener((obs, oldV, newV) -> {
            String filter = (newV == null) ? "" : newV.trim().toLowerCase();

            filteredAutomatak.setPredicate(a ->
                    filter.isEmpty() || a.getCim().toLowerCase().contains(filter));

            filteredCsomagok.setPredicate(c ->
                    filter.isEmpty()
                            || (c.getFelado() != null && c.getFelado().toLowerCase().contains(filter))
                            || (c.getCimzett() != null && c.getCimzett().toLowerCase().contains(filter))
                            || (c.getCelautomata() != null && c.getCelautomata().toLowerCase().contains(filter)));
        });

        // 5) Induláskor az automaták nézet legyen látható
        showAutomatakView();
    }

    // =====================================================
    //       NÉZETVÁLTÓK (automaták <-> csomagok)
    // =====================================================
    private void showAutomatakView() {
        tableAutomatak.setVisible(true);
        tableAutomatak.setManaged(true);

        tableCsomagok.setVisible(false);
        tableCsomagok.setManaged(false);

        lblKarbantartas.setText("Karbantartás\nVálassz ki egy automatát.");
    }

    private void showCsomagokView() {
        tableAutomatak.setVisible(false);
        tableAutomatak.setManaged(false);

        tableCsomagok.setVisible(true);
        tableCsomagok.setManaged(true);

        lblKarbantartas.setText("Csomagok listája");
    }

    // =====================================================
    //                     MENÜGOMBOK
    // =====================================================
    @FXML
    private void handleAutomatakMenu() {
        showAutomatakView();
    }

    @FXML
    private void handleCsomagokMenu() {
        showCsomagokView();
    }

    @FXML
    private void handleSzures() {
        System.out.println("Szűrés panel majd ide jön.");
    }

    @FXML
    private void handleAttekintes() {
        System.out.println("Áttekintés gomb.");
    }

    @FXML
    private void handleModositas() {
        System.out.println("Módosítás funkció ide jön (automatánként).");
    }

    // =====================================================
    //              SERVICE BEÁLLÍTÁSA KÍVÜLRŐL
    // =====================================================
    /*
    public void setService(CsomagService service) {
        this.csomagService = service;

        // Itt már BIZTOSAN NEM null, mert kívülről hívjuk bejelentkezés után
        masterAutomataData.setAll(service.getAutomatak());
        masterCsomagData.setAll(service.getOsszesCsomag());
    }
     */
}
