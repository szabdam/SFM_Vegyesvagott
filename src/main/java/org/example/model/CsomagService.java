package org.example.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/*
    * "Debrecen, Kassai út 26. - Automata #1",
    "Debrecen, Piac utca 10. - Automata #2",
    "Budapest, Kossuth tér 1. - Automata #3"
*/

public class CsomagService {
    public List<Csomag> csomagok;
    public List<Csomagautomata> automatak;

    public CsomagService() {
        this.csomagok = new ArrayList<>();
        this.automatak = new ArrayList<>();

        automatak.add( new Csomagautomata("Debrecen, Kassai út 26. - Automata #1", new ArrayList<>()));
        automatak.add( new Csomagautomata("Debrecen, Piac utca 10. - Automata #2", new ArrayList<>()));
        automatak.add( new Csomagautomata("Budapest, Kossuth tér 1. - Automata #3", new ArrayList<>()));
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
