package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Csomagautomata {
    private String cim;
    private List<Rekesz> rekeszek;

    /**
     * Konstruktor különböző méretű rekeszekhez
     * @param cim Automata címe
     * @param rekeszMeretek Lista a rekeszek méreteivel (pl. "kicsi", "közepes", "nagy")
     */
    public Csomagautomata(String cim, List<String> rekeszMeretek) {
        this.cim = cim;
        this.rekeszek = new ArrayList<>();
        int id = 1;
        for (String meret : rekeszMeretek) {
            rekeszek.add(new Rekesz(id++, meret));
        }
    }

    public String getCim() {
        return cim;
    }

    public List<Rekesz> getRekeszek() {
        return rekeszek;
    }

    public boolean vanSzabadHely() {
        return rekeszek.stream().anyMatch(r -> !r.isFoglalt());
    }

    /**
     * Automatikus csomagelhelyezés az első megfelelő méretű, üres rekeszbe
     * @param csomag Csomag, amelyet el akarunk helyezni
     * @return Rekesz ID, ahová sikerült tenni; -1, ha nincs hely
     */
    public int helyezCsomagot(Csomag csomag) {
        for (Rekesz r : rekeszek) {
            if (!r.isFoglalt() && r.getMeret().equalsIgnoreCase(csomag.getMeret())) {
                r.setFoglalt(true);
                return r.getId();
            }
        }
        return -1; // nincs megfelelő rekesz
    }

    @Override
    public String toString() {
        return "Csomagautomata{" +
                "cím='" + cim + '\'' +
                ", rekeszek=" + rekeszek +
                ", van szabad hely=" + vanSzabadHely() +
                '}';
    }
}
