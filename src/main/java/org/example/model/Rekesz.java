package org.example.model;

public class Rekesz {
    private int id;
    private String meret;
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

