package org.example.repository;

import org.example.model.Csomag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.time.Instant;
import java.util.List;

@Repository
public interface CsomagRepository extends JpaRepository<Csomag, Long> {
    // Keresés méret alapján
    // Pl.: findByMeret("kicsi")
    java.util.List<Csomag> findByMeret(String meret);


    Optional<Csomag> findByFeladasiKod(String feladasiKod);
    Optional<Csomag> findByAtveteliKod(String atveteliKod);
    List<Csomag> findByAllapot(String allapot);
    List<Csomag> findByAllapotAndFoglalasLejarBefore(String allapot, Instant before);
    List<Csomag> findByAllapotAndLetrehozvaBefore(String allapot, Instant before);

    // Random 6 jegyű hex ID keresése/ellenőrzése
    java.util.Optional<Csomag> findByHexId(String hexId);
    boolean existsByHexId(String hexId);

    // Kapcsolt rekesz alapján hivatkozó csomagok számolása (konzisztencia ellenőrzéshez)
    long countByRekeszId(Integer rekeszId);
}
