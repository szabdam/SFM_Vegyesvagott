package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "AUTOMATAK")
public class Csomagautomata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CIM", unique = true, nullable = false)
    private String cim;

    @OneToMany(mappedBy = "automata", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Rekesz> rekeszek = new ArrayList<>();

    public Csomagautomata() {
    }

    public Csomagautomata(String cim) {
        this.cim = cim;
    }

    /**
     * Konstruktor különböző méretű rekeszekhez
     * @param cim Automata címe
     * @param rekeszMeretek Lista a rekeszek méreteivel (pl. "kicsi", "közepes", "nagy")
     */

    public Csomagautomata(String cim, List<String> rekeszMeretek) {
        this.cim = cim;
        rekeszMeretek.forEach(meret -> {
            Rekesz r = new Rekesz(meret);
            addRekesz(r);
        });
    }
    public void addRekesz(Rekesz rekesz) {
        rekeszek.add(rekesz);
        rekesz.setAutomata(this);
    }

    public Long getId() {
        return id;
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
