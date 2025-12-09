package org.example.model;

import jakarta.persistence.*;

@Entity
@Table(name = "CSOMAGOK")
public class Csomag {

    @Column(nullable = false)
    private String meret;// "kicsi", "közepes", "nagy"

    @Column(nullable = false)
    private String cimzett;

    @Column(nullable = false)
    private String felado;

    @Column
    private String megjegyzes;

    @Column(name = "CEL_AUTOMATA")
    private String celautomata;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long azonosito;

    public Csomag() {}

    public Csomag(String meret, String cimzett, String felado, String megjegyzes, String celautomata) {
        this.meret = meret;
        this.cimzett = cimzett;
        this.felado = felado;
        this.megjegyzes = megjegyzes;
        this.celautomata = celautomata;
    }

    public Long getAzonosito() {return azonosito;}

    public void setAzonosito(Long azonosito) {this.azonosito = azonosito;}

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
