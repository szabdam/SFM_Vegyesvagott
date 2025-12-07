package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "REKESZEK")
public class Rekesz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "MERET", nullable = false)
    private String meret;

    @Column(name = "FOGLALT", nullable = false)
    private boolean foglalt;

    public Rekesz(int id, String meret) {
        this.id = id;
        this.meret = meret;
        this.foglalt = false;
    }

    public int getId() { return id; }
    public String getMeret() { return meret; }
    public boolean isFoglalt() { return foglalt; }
    public void setFoglalt(boolean foglalt) { this.foglalt = foglalt; }

    @Override
    public String toString() {
        return "Rekesz{" +
                "id=" + id +
                ", méret='" + meret + '\'' +
                ", foglalt=" + foglalt +
                '}';
    }
}

