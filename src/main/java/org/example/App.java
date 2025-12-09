package org.example;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    private static Stage primaryStage;
    //public static CsomagService globalService = new CsomagService();

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        //Spring context elérés
        var springContext = Launcher.context;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        loader.setControllerFactory(springContext::getBean);

        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());

        stage.setScene(scene);
        stage.setTitle("Csomagkezelés");
        stage.show();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
