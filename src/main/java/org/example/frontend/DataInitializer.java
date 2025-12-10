package org.example.frontend;

import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.example.service.AutomataService;
import org.example.service.CsomagService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadTestData(AutomataService automataService,
                                          CsomagService csomagService,
                                          org.example.service.RekeszService rekeszService,
                                          org.example.repository.CsomagRepository csomagRepository) {
        return args -> {

            if (automataService.getAllAutomatak().isEmpty()) {
                // Helper to create an automata with 20 S, 20 M, 10 L lockers (összesen 50)
                java.util.function.Function<String, Csomagautomata> createWithLockers = cim -> {
                    Csomagautomata a = new Csomagautomata(cim);
                    // 20 small (S)
                    for (int i = 0; i < 20; i++) {
                        a.addRekesz(new Rekesz("S"));
                    }
                    // 20 medium (M)
                    for (int i = 0; i < 20; i++) {
                        a.addRekesz(new Rekesz("M"));
                    }
                    // 10 large (L)
                    for (int i = 0; i < 10; i++) {
                        a.addRekesz(new Rekesz("L"));
                    }
                    return a;
                };
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
            }

            // Backfill/top-up: ha már léteznek automaták, egészítsük ki a rekeszek számát a célkészletre (S20/M20/L10)
            automataService.getAllAutomatak().forEach(a -> {
                int s = 0, m = 0, l = 0;
                if (a.getRekeszek() != null) {
                    for (org.example.model.Rekesz r : a.getRekeszek()) {
                        if ("S".equalsIgnoreCase(r.getMeret())) s++;
                        else if ("M".equalsIgnoreCase(r.getMeret())) m++;
                        else if ("L".equalsIgnoreCase(r.getMeret())) l++;
                    }
                }

                int addS = Math.max(0, 20 - s);
                int addM = Math.max(0, 20 - m);
                int addL = Math.max(0, 10 - l);

                if ((a.getRekeszek() == null || a.getRekeszek().isEmpty()) || addS > 0 || addM > 0 || addL > 0) {
                    for (int i = 0; i < addS; i++) a.addRekesz(new Rekesz("S"));
                    for (int i = 0; i < addM; i++) a.addRekesz(new Rekesz("M"));
                    for (int i = 0; i < addL; i++) a.addRekesz(new Rekesz("L"));
                    if (addS + addM + addL > 0) {
                        automataService.save(a);
                        System.out.println("MIGRATION → Rekeszek kiegészítve az automatán: " + a.getCim()
                                + " (S+" + addS + ", M+" + addM + ", L+" + addL + ")");
                    }
                }
            });

            // Backfill: régi csomagoknál
            //  - ahol az állapot null/üres, állítsuk alapértelmezett értékre
            //  - azonosító: ha hiányzik VAGY nem 6 jegyű decimális (régi hex formátum), generáljunk újat (decimális)
            csomagService.getAllCsomagok().forEach(c -> {
                boolean changed = false;
                if (c.getAllapot() == null || c.getAllapot().isBlank()) {
                    c.setAllapot(org.example.model.Csomag.allapotok.get(0)); // "Feladva"
                    changed = true;
                }
                String currentId = c.getHexId();
                // Ha nincs ID, vagy nem tisztán 6 jegyű decimális (tehát régi hex, pl. tartalmaz A-F-et),
                // akkor nullázzuk, és a saveCsomag generál egy új 6 jegyű decimális ID-t.
                if (currentId == null || currentId.isBlank() || !currentId.matches("\\d{6}")) {
                    c.setHexId(null);
                    changed = true;
                }
                if (changed) {
                    csomagService.saveCsomag(c);
                }
            });

            // Konzisztencia javítás: foglalt rekeszek felszabadítása, ha nincs hozzájuk kapcsolódó csomag
            automataService.getAllAutomatak().forEach(a -> {
                int[] freed = new int[]{0};
                a.getRekeszek().forEach(r -> {
                    if (r.isFoglalt()) {
                        long refs = csomagRepository.countByRekeszId(r.getId());
                        if (refs == 0) {
                            r.setFoglalt(false);
                            rekeszService.save(r);
                            freed[0]++;
                        }
                    }
                });
                if (freed[0] > 0) {
                    System.out.println("CONSISTENCY → Felszabadítottunk " + freed[0] + " árva rekeszt az automatában: " + a.getCim());
                }
            });
        };
    }
}

