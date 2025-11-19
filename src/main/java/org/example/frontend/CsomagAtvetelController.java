package org.example.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.model.CsomagService;

import java.io.IOException;

public class CsomagAtvetelController {
    public javafx.scene.text.Text txtValasz;
    private String IdFromTf;

    CsomagService csomagService =  new CsomagService();

    @FXML
    private void initialize() {}

    public void handleBack(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/org/example/main.fxml"));
        Scene scene = new Scene(root);

        // Stage lekérése a gomb eseményéből
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();

        stage.setScene(scene);
        stage.show();
    }

    public void HandleEnter(ActionEvent actionEvent) {
        IdFromTf = ((TextField) actionEvent.getSource()).getText();
        if (csomagService.isValidID(IdFromTf)) {
            txtValasz.setText("A csomagja az " + csomagService.validIds.get(IdFromTf)+ " automatában található");
        }
        else{txtValasz.setText("Nincs ilyen csomag");}


        System.out.println("IdFromTf: " + IdFromTf);
    }

    /*A csomagja az XYZ automatában található*/
    /*Nincs ilyen csomag*/
}
