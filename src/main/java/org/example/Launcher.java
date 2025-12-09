package org.example;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Launcher {
    public static ConfigurableApplicationContext context;

    public static void main(String[] args) {

        context = new SpringApplicationBuilder(Launcher.class)
                .run(args);

        Application.launch(App.class, args);
    }
    //gui and stuff
}