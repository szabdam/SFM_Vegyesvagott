package org.example.model;

import java.util.*;

/*
    * "Debrecen, Kassai út 26. - Automata #1",
    "Debrecen, Piac utca 10. - Automata #2",
    "Budapest, Kossuth tér 1. - Automata #3"
*/

public class CsomagService {
    public List<Csomag> csomagok;
    public List<Csomagautomata> automatak;
    public HashMap<String, String> validIds;

    public CsomagService() {
        this.csomagok = new ArrayList<>();
        this.automatak = new ArrayList<>();
        this.validIds = new HashMap<>();

        automatak.add( new Csomagautomata("Debrecen, Kassai út 26. - Automata #1", new ArrayList<>()));
        automatak.add( new Csomagautomata("Debrecen, Piac utca 10. - Automata #2", new ArrayList<>()));
        automatak.add( new Csomagautomata("Budapest, Kossuth tér 1. - Automata #3", new ArrayList<>()));

        validIds.put("1", "Debrecen");
        validIds.put("2", "Budapest");
    }

    public Boolean isValidID(String id) {
        return validIds.containsKey(id);
    }

    public void hozzaadCsomag(Csomag csomag) {
        csomagok.add(csomag);
    }

    public void hozzaadAutomata(Csomagautomata automata) {
        automatak.add(automata);
    }

    public List<Csomagautomata>  getAutomatak() {
        return automatak;
    }


    public List<Csomag> getOsszesCsomag() {
        return new ArrayList<>(csomagok);
    }
}
