package org.example.model;

import java.util.*;

public class CsomagService {
    public List<Csomag> csomagok;
    public List<Csomagautomata> automatak;

    // kulcs: csomag azonosító (String), érték: automata címe
    public HashMap<String, String> validIds;

    public CsomagService() {
        this.csomagok = new ArrayList<>();
        this.automatak = new ArrayList<>();
        this.validIds = new HashMap<>();

        automatak.add(new Csomagautomata(
                "Debrecen, Kassai út 26. - Automata #1",
                new ArrayList<>()
        ));
        automatak.add(new Csomagautomata(
                "Debrecen, Piac utca 10. - Automata #2",
                new ArrayList<>()
        ));
        automatak.add(new Csomagautomata(
                "Budapest, Kossuth tér 1. - Automata #3",
                new ArrayList<>()
        ));
    }

    // Megnézi, hogy létezik-e ilyen csomagazonosító
    public boolean isValidID(String id) {
        if (id == null) {
            return false;
        }
        return validIds.containsKey(id);
    }

    // Visszaadja az automata nevét az azonosító alapján
    public String getAutomataNev(String id) {
        if (id == null) {
            return null;
        }
        return validIds.get(id);
    }

    public void hozzaadCsomag(Csomag csomag) {
        if (csomag != null) {
            csomagok.add(csomag);
        }
    }

    public void hozzaadAutomata(Csomagautomata automata) {
        if (automata != null) {
            automatak.add(automata);
        }
    }

    public List<Csomagautomata> getAutomatak() {
        return automatak;
    }

    public List<Csomag> getOsszesCsomag() {
        return new ArrayList<>(csomagok);
    }
}
