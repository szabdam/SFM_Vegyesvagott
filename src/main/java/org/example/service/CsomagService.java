package org.example.service;

import org.example.model.Csomag;
import java.util.List;

public interface CsomagService {



    boolean isValidID(String id);

    String getAutomataNev(String id);

    Csomag saveCsomag(Csomag csomag);

    // --- Azonosító (hex‑ID) -------------------------------------------------
    /** Keresi a csomagot a 6‑jegyű hex‑ID alapján. */
    Csomag getByHexId(String hexId);

    // (régi getCsomagById(Long) és getCsomagById(String) metódusok eltávolítva)

    List<Csomag> getAllCsomagok();

    void deleteCsomag(String id);


    // Admin módosító ablakból történő mentéshez
    Csomag updateCsomagAdmin(String id,
                              String ujFelado,
                              String ujCimzett,
                              String ujCelAutomata,
                              String ujAllapot);

    // --- ÚJ: Szolgáltatási folyamatok ---
    Csomag inditFeladas(String meret,
                        String kategoria,
                        String felado,
                        String cimzett,
                        String celAutomataCim,
                        String megjegyzes,
                        Long feladasAutomataId);

    Csomag feladasElhelyezes(String feladasiKod);

    java.util.List<Csomag> listAllapot(String allapot);

    void inditSzallitas(java.util.List<Long> csomagIds);

    Csomag celraErkezett(Long csomagId);

    Csomag atvetel(String atveteliKod);

    // Kapacitás
    boolean isAutomataFull(Long automataId);
    java.util.List<org.example.model.Csomagautomata> listTeltAutomatak();
    java.util.List<Csomag> list48OrasAtNemVett();
}
