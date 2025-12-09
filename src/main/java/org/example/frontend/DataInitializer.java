package org.example.frontend;

import org.example.model.Csomagautomata;
import org.example.service.AutomataService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadTestData(AutomataService automataService) {
        return args -> {

            if (automataService.getAllAutomatak().isEmpty()) {

                automataService.save(new Csomagautomata("Debrecen, Kassai út 26. - Automata #1"));
                automataService.save(new Csomagautomata("Debrecen, Piac utca 10. - Automata #2"));
                automataService.save(new Csomagautomata("Budapest, Kossuth tér 1. - Automata #3"));

                System.out.println("SEED → Automaták beletöltve az adatbázisba.");
            }
        };
    }
}

