package org.example.service;

import org.example.model.Csomag;
import org.example.model.Csomagautomata;
import org.example.repository.CsomagRepository;

import org.example.repository.CsomagautomataRepository;
import org.example.service.CsomagService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class CsomagServiceImpl implements CsomagService {

    private final CsomagRepository csomagRepository;
    private final CsomagautomataRepository automataRepository;

    private final HashMap<String, String> validIds = new HashMap<>();

    public CsomagServiceImpl(CsomagRepository csomagRepository,
                             CsomagautomataRepository automataRepository) {
        this.csomagRepository = csomagRepository;
        this.automataRepository = automataRepository;

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
    public void deleteCsomag(Long id) {
        csomagRepository.deleteById(id);
    }
}
