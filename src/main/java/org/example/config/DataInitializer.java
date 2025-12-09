package org.example.config;

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

            System.out.println(">>> DataInitializer fut!");

            if (automataService.getAllAutomatak().isEmpty()) {

                automataService.save(new Csomagautomata("Debrecen, Kassai út 26. - Automata #1"));
                automataService.save(new Csomagautomata("Debrecen, Piac utca 10. - Automata #2"));
                automataService.save(new Csomagautomata("Budapest, Kossuth tér 1. - Automata #3"));
                automataService.save(new Csomagautomata("Budapest, Rákóczi út 12. - Automata #4"));
                automataService.save(new Csomagautomata("Budapest, Andrássy út 98. - Automata #5"));
                automataService.save(new Csomagautomata("Budapest, Bartók Béla út 56. - Automata #6"));
                automataService.save(new Csomagautomata("Budapest, Üllői út 201. - Automata #7"));
                automataService.save(new Csomagautomata("Győr, Szent István út 34. - Automata #8"));
                automataService.save(new Csomagautomata("Győr, Baross Gábor út 15. - Automata #9"));
                automataService.save(new Csomagautomata("Pécs, Király utca 2. - Automata #10"));
                automataService.save(new Csomagautomata("Pécs, Rákóczi út 66. - Automata #11"));
                automataService.save(new Csomagautomata("Szeged, Kárász utca 11. - Automata #12"));
                automataService.save(new Csomagautomata("Szeged, Londoni körút 35. - Automata #13"));
                automataService.save(new Csomagautomata("Miskolc, Széchenyi utca 47. - Automata #14"));
                automataService.save(new Csomagautomata("Miskolc, Kiss Ernő utca 4. - Automata #15"));
                automataService.save(new Csomagautomata("Nyíregyháza, Kossuth tér 9. - Automata #16"));
                automataService.save(new Csomagautomata("Nyíregyháza, Sóstói út 112. - Automata #17"));
                automataService.save(new Csomagautomata("Eger, Dobó tér 1. - Automata #18"));
                automataService.save(new Csomagautomata("Eger, Kossuth Lajos utca 23. - Automata #19"));
                automataService.save(new Csomagautomata("Szolnok, Ady Endre út 52. - Automata #20"));
                automataService.save(new Csomagautomata("Szolnok, Baross utca 19. - Automata #21"));
                automataService.save(new Csomagautomata("Veszprém, Kossuth Lajos utca 10. - Automata #22"));
                automataService.save(new Csomagautomata("Veszprém, Budapest út 33. - Automata #23"));
                System.out.println("SEED → Automaták beletöltve az adatbázisba.");
            }else {
                System.out.println("SEED → Nem töltök be adatot, mert már van automata az adatbázisban.");
            }
        };
    }
}

