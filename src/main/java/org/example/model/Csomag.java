package org.example.model;

public class Csomag {
    private String meret; // "kicsi", "közepes", "nagy"
    private String hovaMegy;
    private double suly;

    public Csomag(String meret, String hovaMegy, double suly) {
        this.meret = meret;
        this.hovaMegy = hovaMegy;
        this.suly = suly;
    }

    public String getMeret() { return meret; }
    public String getHovaMegy() { return hovaMegy; }
    public double getSuly() { return suly; }

    @Override
    public String toString() {
        return "Csomag{" +
                "méret='" + meret + '\'' +
                ", hová megy='" + hovaMegy + '\'' +
                ", súly=" + suly +
                " kg}";
    }
}
