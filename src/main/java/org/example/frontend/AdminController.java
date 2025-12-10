package org.example.frontend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.stage.Stage;
import javafx.stage.Modality;
import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.example.service.AutomataService;
import org.example.service.CsomagService;
import org.example.service.RekeszService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import org.example.service.AutomataService;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.scene.layout.Region;


@Component
public class AdminController {


    @Autowired
    private CsomagService csomagService;

    @Autowired
    private AutomataService automataService;

    @Autowired
    private RekeszService rekeszService;

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

    @FXML
    private TableColumn<Csomag, String> colAllapot1;

    // ------------ CSOMAGOK TÁBLÁZAT ------------
    @FXML
    private TableView<Csomag> tableCsomagok;

    @FXML
    private TableColumn<Csomag, String> colId;

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

    // Szűrés állapotok
    private boolean onlyFreeAutomata = false;     // Automatáknál: csak szabad hellyel rendelkezők
    private boolean onlyOpenCsomagok = false;     // Csomagoknál: még nem átvett csomagok

    // =====================================================
    //                    INITIALIZE()
    // =====================================================
    @FXML
    private void initialize() {

        // FXML-ben a CONSTRAINED_RESIZE_POLICY konstans néha nem kerül helyesen feloldásra,
        // ezért programból állítjuk be a Csomagok táblára.
        if (tableCsomagok != null) {
            tableCsomagok.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        // 1) Automaták oszlopok
        colCim.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCim()));

        colTelitettseg.setCellValueFactory(cell -> {
            Csomagautomata a = cell.getValue();
            long foglalt = rekeszService.countFoglalt(a.getId());
            long szabad = rekeszService.countSzabad(a.getId());
            long total = foglalt + szabad;
            int percent = total == 0 ? 0 : (int) Math.round(foglalt * 100.0 / (double) total);
            return new SimpleStringProperty(percent + " %");
        });

        colSzabad.setCellValueFactory(cell -> {
            Csomagautomata a = cell.getValue();
            int free = (int) rekeszService.countSzabad(a.getId());
            return new SimpleIntegerProperty(free).asObject();
        });

        colAllapot.setCellValueFactory(cell -> {
            Csomagautomata a = cell.getValue();
            boolean vanSzabad = rekeszService.countSzabad(a.getId()) > 0;
            return new SimpleStringProperty(vanSzabad ? "OK" : "TELT");
        });

        // 2) Csomagok oszlopok
        if (colId != null) {
            colId.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getHexId()));
        }
        colFelado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFelado()));

        colCimzett.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCimzett()));

        colCelautomata.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getCelautomata()));

        colAllapot1.setCellValueFactory(c -> {
            String state = c.getValue().getAllapot();
            return new SimpleStringProperty((state == null || state.isBlank()) ? "" : state);
        });

        // --- Adatok betöltése JPA-ból ---
        masterAutomataData.setAll(automataService.getAllAutomatak());
        masterCsomagData.setAll(csomagService.getAllCsomagok());

        // 3) Filterelt listák (egyelőre üres master listákon)
        filteredAutomatak = new FilteredList<>(masterAutomataData, p -> true);
        filteredCsomagok = new FilteredList<>(masterCsomagData, p -> true);

        // 3/a) Rendezettség biztosítása (oszlopfejlécre kattintva rendezhető)
        SortedList<Csomagautomata> sortedAutomatak = new SortedList<>(filteredAutomatak);
        sortedAutomatak.comparatorProperty().bind(tableAutomatak.comparatorProperty());
        tableAutomatak.setItems(sortedAutomatak);

        SortedList<Csomag> sortedCsomagok = new SortedList<>(filteredCsomagok);
        sortedCsomagok.comparatorProperty().bind(tableCsomagok.comparatorProperty());
        tableCsomagok.setItems(sortedCsomagok);

        // 4) Kereső: mindkét listát szűri
        tfKereses.textProperty().addListener((obs, oldV, newV) -> applyFilters());

        // 5) Induláskor az automaták nézet legyen látható
        showAutomatakView();
    }

    // =====================================================
    //       NÉZETVÁLTÓK (automaták <-> csomagok)
    // =====================================================
    private void showAutomatakView() {
        // Always refresh data when switching views so telítettség and állapot are up to date
        loadData();
        tableAutomatak.setVisible(true);
        tableAutomatak.setManaged(true);

        tableCsomagok.setVisible(false);
        tableCsomagok.setManaged(false);

        lblKarbantartas.setText("Karbantartás\nVálassz ki egy automatát.");
        // Update filter button text to reflect current automata filter state
        btnSzures.setText(onlyFreeAutomata ? "Szűrés: csak szabad" : "Szűrés");
    }

    private void showCsomagokView() {
        // Always refresh data when switching views so telítettség and állapot are up to date
        loadData();
        tableAutomatak.setVisible(false);
        tableAutomatak.setManaged(false);

        tableCsomagok.setVisible(true);
        tableCsomagok.setManaged(true);

        lblKarbantartas.setText("Csomagok listája");
        // Update filter button text to reflect current package filter state
        btnSzures.setText(onlyOpenCsomagok ? "Szűrés: nyitott" : "Szűrés");
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
        // Nézetfüggő kapcsoló
        if (tableAutomatak.isVisible()) {
            onlyFreeAutomata = !onlyFreeAutomata;
            btnSzures.setText(onlyFreeAutomata ? "Szűrés: csak szabad" : "Szűrés");
        } else {
            onlyOpenCsomagok = !onlyOpenCsomagok;
            btnSzures.setText(onlyOpenCsomagok ? "Szűrés: nyitott" : "Szűrés");
        }
        applyFilters();
    }

    @FXML
    private void handleAttekintes() {
        if (tableAutomatak.isVisible()) {
            showAutomataOverview();
        } else {
            showCsomagOverview();
        }
    }

    @FXML
    private void handleModositas() {
        if (tableCsomagok.isVisible()) {
            Csomag selected = tableCsomagok.getSelectionModel().getSelectedItem();
            if (selected == null) {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setTitle("Nincs kiválasztva");
                a.setHeaderText(null);
                a.setContentText("Válassz ki egy csomagot a módosításhoz.");
                a.showAndWait();
                return;
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/csomagModositas.fxml"));
                loader.setControllerFactory(org.example.Launcher.context::getBean);
                Parent root = loader.load();

                CsomagModositasController controller = loader.getController();
                controller.setCsomag(selected);

                Stage dialog = new Stage();
                dialog.setTitle("Csomag módosítása");
                dialog.initOwner(btnModositas.getScene().getWindow());
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setScene(new Scene(root));
                dialog.showAndWait();

                // Frissítsük a listát bezárás után
                loadData();
            } catch (IOException e) {
                e.printStackTrace();
                Alert a = new Alert(Alert.AlertType.ERROR, "Nem sikerült megnyitni a módosító ablakot: " + e.getMessage());
                a.showAndWait();
            }
        } else {
            // Automaták nézetben később külön szerkesztő nyitható
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("Információ");
            a.setHeaderText(null);
            a.setContentText("A módosítás jelenleg a Csomagok nézetben érhető el.");
            a.showAndWait();
        }
    }

    @FXML
    private void handleKilepes() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/main.fxml"));

        // Spring esetén:
        loader.setControllerFactory(org.example.Launcher.context::getBean);

        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) btnAutomatak.getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void loadData() {
        masterAutomataData.setAll(automataService.getAllAutomatak());
        masterCsomagData.setAll(csomagService.getAllCsomagok());
        applyFilters();
    }

    // =====================================================
    //                   SEGÉD MÓDSZEREK
    // =====================================================
    private void applyFilters() {
        String filter = (tfKereses.getText() == null) ? "" : tfKereses.getText().trim().toLowerCase();

        // Automaták kombinált predikátuma
        Predicate<Csomagautomata> searchAutomata = a ->
                filter.isEmpty() || (a.getCim() != null && a.getCim().toLowerCase().contains(filter));
        Predicate<Csomagautomata> freeOnly = a -> !onlyFreeAutomata || a.vanSzabadHely();
        filteredAutomatak.setPredicate(searchAutomata.and(freeOnly));

        // Csomagok kombinált predikátuma
        Predicate<Csomag> searchCsomag = c -> {
            if (filter.isEmpty()) return true;
            String hexId = c.getHexId();
            String felado = c.getFelado();
            String cimzett = c.getCimzett();
            String cel = c.getCelautomata();
            return (hexId != null && hexId.toLowerCase().contains(filter))
                    || (felado != null && felado.toLowerCase().contains(filter))
                    || (cimzett != null && cimzett.toLowerCase().contains(filter))
                    || (cel != null && cel.toLowerCase().contains(filter));
        };
        Predicate<Csomag> openOnly = c -> !onlyOpenCsomagok || (c.getAllapot() == null || !"Átvéve".equalsIgnoreCase(c.getAllapot()));
        filteredCsomagok.setPredicate(searchCsomag.and(openOnly));
    }

    private void showAutomataOverview() {
        var items = tableAutomatak.getItems(); // SortedList
        int automataCount = items.size();
        int totalRekesz = items.stream().mapToInt(a -> a.getRekeszek().size()).sum();
        int foglalt = items.stream().mapToInt(a -> (int) a.getRekeszek().stream().filter(Rekesz::isFoglalt).count()).sum();
        int szabad = totalRekesz - foglalt;
        long ok = items.stream().filter(Csomagautomata::vanSzabadHely).count();
        long telt = automataCount - ok;

        String msg = "Automaták áttekintése:\n"
                + "Darab: " + automataCount + "\n"
                + "Összes rekesz: " + totalRekesz + "\n"
                + "Foglalt: " + foglalt + ", Szabad: " + szabad + "\n"
                + "OK: " + ok + ", TELT: " + telt;

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Áttekintés");
        alert.setHeaderText("Automaták");
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showCsomagOverview() {
        var items = tableCsomagok.getItems(); // SortedList
        int count = items.size();
        Map<String, Long> byAllapot = items.stream()
                .collect(Collectors.groupingBy(c -> {
                    String a = c.getAllapot();
                    return (a == null || a.isBlank()) ? "(nincs megadva)" : a;
                }, Collectors.counting()));
        Map<String, Long> byMeret = items.stream()
                .collect(Collectors.groupingBy(Csomag::getMeret, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        sb.append("Csomagok áttekintése:\n");
        sb.append("Összesen: ").append(count).append("\n\n");

        sb.append("Állapot szerint:\n");
        byAllapot.forEach((k,v) -> sb.append(" - ").append(k).append(": ").append(v).append("\n"));
        sb.append("\nMéret szerint:\n");
        byMeret.forEach((k,v) -> sb.append(" - ").append(k).append(": ").append(v).append("\n"));

        // Megjegyzések listája (nem üres)
        String comments = items.stream()
                .filter(c -> c.getMegjegyzes() != null && !c.getMegjegyzes().isBlank())
                .map(c -> {
                    String id = c.getHexId();
                    String cimzett = c.getCimzett() == null ? "" : c.getCimzett();
                    String note = c.getMegjegyzes();
                    String who = (id != null && !id.isBlank() ? "#" + id + " " : "") + (cimzett.isBlank() ? "" : ("[" + cimzett + "] "));
                    return " - " + who + note;
                })
                .collect(Collectors.joining("\n"));

        sb.append("\nMegjegyzések:\n");
        if (comments.isBlank()) {
            sb.append(" - (nincs megjegyzés)");
        } else {
            sb.append(comments);
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Áttekintés");
        alert.setHeaderText("Csomagok");
        alert.setContentText(sb.toString());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}
