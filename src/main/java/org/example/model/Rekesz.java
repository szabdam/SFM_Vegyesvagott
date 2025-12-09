package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "REKESZEK")
public class Rekesz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String meret;

    @Column(nullable = false)
    private boolean foglalt = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "automata_id")
    private Csomagautomata automata;

    public Rekesz() {}

    public Rekesz(String meret) {
        this.meret = meret;
        this.foglalt = false;
    }

    public int getId() { return id; }
    public String getMeret() { return meret; }
    public boolean isFoglalt() { return foglalt; }
    public void setFoglalt(boolean foglalt) { this.foglalt = foglalt; }

    public Csomagautomata getAutomata() {
        return automata;
    }

    public void setAutomata(Csomagautomata automata) {
        this.automata = automata;
    }

    @Override
    public String toString() {
        return "Rekesz{" +
                "id=" + id +
                ", méret='" + meret + '\'' +
                ", foglalt=" + foglalt +
                '}';
    }
}

