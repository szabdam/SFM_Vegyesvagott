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

                automataService.save(createWithLockers.apply("Debrecen, Kassai út 26. - Automata #1"));
                automataService.save(createWithLockers.apply("Debrecen, Piac utca 10. - Automata #2"));
                automataService.save(createWithLockers.apply("Budapest, Kossuth tér 1. - Automata #3"));

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

