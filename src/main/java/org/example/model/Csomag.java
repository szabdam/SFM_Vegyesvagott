package org.example.model;

public class Csomag {
    private String meret; // "kicsi", "közepes", "nagy"
    private String cimzett;
    private String felado;
    private String megjegyzes;
    private String celautomata;
    private int azonosito;

    public Csomag(String meret, String cimzett, String felado, String megjegyzes, String celautomata, int azonosito) {
        this.meret = meret;
        this.cimzett = cimzett;
        this.felado = felado;
        this.megjegyzes = megjegyzes;
        this.celautomata = celautomata;
        this.azonosito = azonosito;
    }

    public int getAzonosito() {return azonosito;}

    public void setAzonosito(int azonosito) {this.azonosito = azonosito;}

    public String getMeret() {return meret;}

    public void setMeret(String meret) {this.meret = meret;}

    public String getCimzett() {return cimzett;}

    public void setCimzett(String cimzett) {this.cimzett = cimzett;}

    public String getFelado() {return felado;}

    public void setFelado(String felado) {this.felado = felado;}

    public String getMegjegyzes() {return megjegyzes;}

    public void setMegjegyzes(String megjegyzes) {this.megjegyzes = megjegyzes;}

    public String getCelautomata() {return celautomata;}

    public void setCelautomata(String celautomata) {this.celautomata = celautomata;}

    @Override
    public String toString() {
        return "Csomag{" +
                "meret='" + meret + '\'' +
                ", cimzett='" + cimzett + '\'' +
                ", felado='" + felado + '\'' +
                ", megjegyzes='" + megjegyzes + '\'' +
                ", celautomata='" + celautomata + '\'' +
                ", azonosito='" + azonosito +
                '}';
    }
}
