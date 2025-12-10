package org.example.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.Instant;

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

    @Column
    private String allapot;

    // --- Új mezők a szolgáltatási folyamatokhoz ---
    @Column(name = "FELADASI_KOD")
    private String feladasiKod;      // kód, amivel a feladó elhelyezi a csomagot

    @Column(name = "ATVETELI_KOD")
    private String atveteliKod;      // kód, amivel a címzett átveszi

    @Column(name = "FOGLALAS_LEJAR")
    private Instant foglalasLejar;   // lefoglalás lejáratának ideje (24h)

    @Column(name = "LETREHOZVA")
    private Instant letrehozva;      // létrehozás ideje

    @Column(name = "REKESZ_ID")
    private Integer rekeszId;        // opcionális: melyik rekeszhez kötjük


    public static final List<String> allapotok = Arrays.asList(
            "Feladva",
            "Begyűjtve",
            "Raktárban",
            "Kiszállítás alatt",
            "Átvételre kész",
            "Átvéve"
    );
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long azonosito;

    // 6 jegyű, nagybetűs hexadecimális azonosító (véletlenszerűen generált)
    @Column(name = "HEX_ID", length = 6, unique = true)
    private String hexId;

    public Csomag() {}

    public Csomag(String meret, String cimzett, String felado, String megjegyzes, String celautomata) {
        this.meret = meret;
        this.cimzett = cimzett;
        this.felado = felado;
        this.megjegyzes = megjegyzes;
        this.celautomata = celautomata;
        this.allapot = allapotok.get(0);
    }

    public Long getAzonosito() {return azonosito;}

    // Megjelenítéshez: 6 jegyű hexadecimális azonosító (null esetén üres string)
    @Transient
    public String getHexId() {
        return hexId == null ? "" : hexId;
    }

    public void setHexId(String hexId) { this.hexId = hexId; }

    public void moveForward() {
        // Haladjon a következő állapotra biztonságosan
        if (allapot == null) {
            // Ha még nincs állapot, beállítjuk az elsőt
            this.allapot = allapotok.get(0);
            return;
        }
        int idx = allapotok.indexOf(this.allapot);
        if (idx < 0) {
            // Ismeretlen állapot esetén visszaállunk az elsőre
            this.allapot = allapotok.get(0);
            return;
        }
        if (idx < allapotok.size() - 1) {
            this.allapot = allapotok.get(idx + 1);
        }
        // Ha az utolsó állapoton vagyunk, maradunk ott (nincs wrap)
    }

    public String getAllapot() {return allapot;}

    public void setAllapot(String allapot) { this.allapot = allapot; }


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

    public String getFeladasiKod() { return feladasiKod; }

    public void setFeladasiKod(String feladasiKod) { this.feladasiKod = feladasiKod; }

    public String getAtveteliKod() { return atveteliKod; }

    public void setAtveteliKod(String atveteliKod) { this.atveteliKod = atveteliKod; }

    public Instant getFoglalasLejar() { return foglalasLejar; }

    public void setFoglalasLejar(Instant foglalasLejar) { this.foglalasLejar = foglalasLejar; }

    public Instant getLetrehozva() { return letrehozva; }

    public void setLetrehozva(Instant letrehozva) { this.letrehozva = letrehozva; }

    public Integer getRekeszId() { return rekeszId; }

    public void setRekeszId(Integer rekeszId) { this.rekeszId = rekeszId; }

    @PrePersist
    private void onCreate() {
        this.letrehozva = Instant.now();
    }

    @Override
    public String toString() {
        return "Csomag{" +
                "meret='" + meret + '\'' +
                ", cimzett='" + cimzett + '\'' +
                ", felado='" + felado + '\'' +
                ", megjegyzes='" + megjegyzes + '\'' +
                ", celautomata='" + celautomata + '\'' +
                ", allapot='" + allapot + '\'' +
                ", azonosito='" + azonosito +
                '}';
    }
}
