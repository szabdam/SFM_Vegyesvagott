package org.example.service;

import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.example.repository.CsomagRepository;

import org.example.repository.CsomagautomataRepository;
import org.example.repository.RekeszRepository;
import org.example.service.CsomagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.time.Instant;
import java.security.SecureRandom;

@Service
public class CsomagServiceImpl implements CsomagService {

    private final CsomagRepository csomagRepository;
    private final CsomagautomataRepository automataRepository;
    private final RekeszRepository rekeszRepository;

    private final HashMap<String, String> validIds = new HashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();

    public CsomagServiceImpl(CsomagRepository csomagRepository,
                             CsomagautomataRepository automataRepository,
                             RekeszRepository rekeszRepository) {
        this.csomagRepository = csomagRepository;
        this.automataRepository = automataRepository;
        this.rekeszRepository = rekeszRepository;

        // Manuális seed (ha szükséges)
        validIds.put("A123", "Debrecen, Kassai út 26. - Automata #1");
        validIds.put("B555", "Budapest, Kossuth tér 1. - Automata #3");
    }

    @Override
    public Csomag getByAzonosito(Long id) {
        return csomagRepository.findByAzonosito(id);
    }

    @Override
    public boolean isValidID(String id) {
        if (id == null) return false;
        return validIds.containsKey(id);
    }

    @Override
    public String getAutomataNev(String id) {
        if (id == null) return null;
        return validIds.get(id);
    }

    @Override
    public Csomag saveCsomag(Csomag csomag) {
        // Biztosítsunk 6 jegyű véletlen decimális azonosítót, ha még nincs
        if (csomag != null && (csomag.getHexId() == null || csomag.getHexId().isBlank())) {
            csomag.setHexId(generateUniqueHex6());
        }
        // Megpróbáljuk azonnal elhelyezni a csomagot a kiválasztott automatában méret szerint
        if (csomag.getCelautomata() != null && csomag.getMeret() != null) {
            Csomagautomata automata = automataRepository.findByCim(csomag.getCelautomata());
            if (automata != null) {
                List<Rekesz> freeBySize = rekeszRepository
                        .findByAutomataIdAndMeretAndFoglaltFalse(automata.getId(), csomag.getMeret());
                if (!freeBySize.isEmpty()) {
                    Rekesz r = freeBySize.get(0);
                    r.setFoglalt(true); // telítettség +1
                    rekeszRepository.save(r);
                    csomag.setRekeszId(r.getId());
                }
            }
        }
        return csomagRepository.save(csomag);
    }

    @Override
    public Csomag getCsomagById(Long id) {
        return csomagRepository.findById(id).orElse(null);
    }

    @Override
    public List<Csomag> getAllCsomagok() {
        return csomagRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteCsomag(Long id) {
        // Free locker if exists, then delete
        csomagRepository.findById(id).ifPresent(c -> {
            Integer rekeszId = c.getRekeszId();
            if (rekeszId != null) {
                rekeszRepository.findById(rekeszId.longValue()).ifPresent(r -> {
                    r.setFoglalt(false);
                    rekeszRepository.save(r);
                });
            }
        });
        csomagRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Csomag updateCsomagAdmin(Long id,
                                    String ujFelado,
                                    String ujCimzett,
                                    String ujCelAutomata,
                                    String ujAllapot) {
        Csomag cs = csomagRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Nincs ilyen csomag: " + id));

        String regiCel = cs.getCelautomata();
        String regiAllapot = cs.getAllapot();
        Integer regiRekeszId = cs.getRekeszId();

        // Alap adatok frissítése
        cs.setFelado(ujFelado);
        cs.setCimzett(ujCimzett);
        cs.setCelautomata(ujCelAutomata);
        cs.setAllapot(ujAllapot);

        boolean celValtozott = (regiCel == null && ujCelAutomata != null)
                || (regiCel != null && !regiCel.equals(ujCelAutomata));

        // Ha cél automata változott: régi rekesz felszabadítása, újban hely foglalása méret szerint
        if (celValtozott) {
            if (regiRekeszId != null) {
                rekeszRepository.findById(regiRekeszId.longValue()).ifPresent(r -> {
                    r.setFoglalt(false);
                    rekeszRepository.save(r);
                });
                cs.setRekeszId(null);
            }
            if (ujCelAutomata != null) {
                Csomagautomata uj = automataRepository.findByCim(ujCelAutomata);
                if (uj != null) {
                    List<Rekesz> free = rekeszRepository
                            .findByAutomataIdAndMeretAndFoglaltFalse(uj.getId(), cs.getMeret());
                    if (!free.isEmpty()) {
                        Rekesz r = free.get(0);
                        r.setFoglalt(true);
                        rekeszRepository.save(r);
                        cs.setRekeszId(r.getId());
                    }
                }
            }
        }

        // Ha állapot Átvéve lett: rekeszt felszabadítjuk
        if ("Átvéve".equalsIgnoreCase(ujAllapot)) {
            Integer rekeszId = cs.getRekeszId();
            if (rekeszId != null) {
                rekeszRepository.findById(rekeszId.longValue()).ifPresent(r -> {
                    r.setFoglalt(false);
                    rekeszRepository.save(r);
                });
                cs.setRekeszId(null);
            }
        }

        // Ha állapot Átvételre kész és nincs rekesz, próbáljunk foglalni a jelenlegi cél automatában
        if ("Átvételre kész".equalsIgnoreCase(ujAllapot) && cs.getRekeszId() == null && cs.getCelautomata() != null) {
            Csomagautomata cel = automataRepository.findByCim(cs.getCelautomata());
            if (cel != null) {
                List<Rekesz> free = rekeszRepository
                        .findByAutomataIdAndMeretAndFoglaltFalse(cel.getId(), cs.getMeret());
                if (!free.isEmpty()) {
                    Rekesz r = free.get(0);
                    r.setFoglalt(true);
                    rekeszRepository.save(r);
                    cs.setRekeszId(r.getId());
                }
            }
        }

        return csomagRepository.save(cs);
    }

    // ===================== ÚJ FUNKCIÓK =====================

    @Override
    @Transactional
    public Csomag inditFeladas(String meret,
                               String kategoria,
                               String felado,
                               String cimzett,
                               String celAutomataCim,
                               String megjegyzes,
                               Long feladasAutomataId) {
        // alap ellenőrzések
        if (meret == null || felado == null || cimzett == null || celAutomataCim == null) {
            throw new IllegalArgumentException("Hiányzó kötelező adatok");
        }

        Csomag cs = new Csomag(meret, cimzett, felado, megjegyzes, celAutomataCim);
        // 6 jegyű decimális azonosító
        cs.setHexId(generateUniqueHex6());
        cs.setAllapot("Lefoglalva");
        cs.setFeladasiKod(genCode());
        cs.setFoglalasLejar(Instant.now().plusSeconds(24 * 3600));

        // kapacitás: ha meg van adva feladási automata, próbáljunk szabad rekeszt találni MÉRET szerint
        if (feladasAutomataId != null) {
            List<Rekesz> free = rekeszRepository.findByAutomataIdAndMeretAndFoglaltFalse(feladasAutomataId, meret);
            if (!free.isEmpty()) {
                Rekesz r = free.get(0);
                r.setFoglalt(true);
                rekeszRepository.save(r);
                cs.setRekeszId(r.getId());
            }
        }

        return csomagRepository.save(cs);
    }

    @Override
    @Transactional
    public Csomag feladasElhelyezes(String feladasiKod) {
        Csomag cs = csomagRepository.findByFeladasiKod(feladasiKod)
                .orElseThrow(() -> new IllegalArgumentException("Érvénytelen feladási kód"));
        cs.setAllapot("Feladva");
        cs.setFoglalasLejar(null); // innentől nincs foglalási lejárat
        return csomagRepository.save(cs);
    }

    @Override
    public List<Csomag> listAllapot(String allapot) {
        return csomagRepository.findByAllapot(allapot);
    }

    @Override
    @Transactional
    public void inditSzallitas(List<Long> csomagIds) {
        for (Long id : csomagIds) {
            Csomag cs = csomagRepository.findById(id).orElse(null);
            if (cs == null) continue;
            cs.setAllapot("Kiszállítás alatt");
            csomagRepository.save(cs);
        }
    }

    @Override
    @Transactional
    public Csomag celraErkezett(Long csomagId) {
        Csomag cs = csomagRepository.findById(csomagId)
                .orElseThrow(() -> new IllegalArgumentException("Nincs ilyen csomag"));

        // próbálunk szabad rekeszt keresni a cél automatában (név alapján) MÉRET szerint
        Csomagautomata cel = automataRepository.findByCim(cs.getCelautomata());
        if (cel != null) {
            List<Rekesz> free = rekeszRepository.findByAutomataIdAndMeretAndFoglaltFalse(cel.getId(), cs.getMeret());
            if (!free.isEmpty()) {
                Rekesz r = free.get(0);
                r.setFoglalt(true);
                rekeszRepository.save(r);
                cs.setRekeszId(r.getId());
            }
        }
        cs.setAllapot("Átvételre kész");
        cs.setAtveteliKod(genCode());
        return csomagRepository.save(cs);
    }

    @Override
    @Transactional
    public Csomag atvetel(String atveteliKod) {
        Csomag cs = csomagRepository.findByAtveteliKod(atveteliKod)
                .orElseThrow(() -> new IllegalArgumentException("Érvénytelen átvételi kód"));

        // felszabadítjuk a rekeszt, ha ismert
        Integer rekeszId = cs.getRekeszId();
        if (rekeszId != null) {
            rekeszRepository.findById(rekeszId.longValue()).ifPresent(r -> {
                r.setFoglalt(false);
                rekeszRepository.save(r);
            });
            cs.setRekeszId(null);
        }
        cs.setAllapot("Átvéve");
        return csomagRepository.save(cs);
    }

    // ===== Helpers =====
    private String generateUniqueHex6() {
        // 6 jegyű decimális ID (000000 - 999999), ütközés ellenőrzéssel
        for (int i = 0; i < 20; i++) {
            String candidate = randomDec6();
            if (!csomagRepository.existsByHexId(candidate)) {
                return candidate;
            }
        }
        // Fallback: utolsó generált érték visszaadása
        return randomDec6();
    }

    private String randomDec6() {
        int v = secureRandom.nextInt(1_000_000); // 0..999999
        return String.format("%06d", v);
    }

    // -------- Kapacitás --------
    @Override
    public boolean isAutomataFull(Long automataId) {
        return rekeszRepository.countByAutomataIdAndFoglaltFalse(automataId) == 0;
    }

    @Override
    public List<Csomagautomata> listTeltAutomatak() {
        return automataRepository.findAll().stream()
                .filter(a -> rekeszRepository.countByAutomataIdAndFoglaltFalse(a.getId()) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public List<Csomag> list48OrasAtNemVett() {
        Instant limit = Instant.now().minusSeconds(48 * 3600);
        return csomagRepository.findByAllapotAndLetrehozvaBefore("Átvételre kész", limit);
    }

    // -------- Segéd --------
    private String genCode() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) sb.append(chars.charAt(rnd.nextInt(chars.length())));
        return sb.toString();
    }
}
