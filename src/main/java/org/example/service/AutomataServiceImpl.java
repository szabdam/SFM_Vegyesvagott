package org.example.service;

import org.example.model.Csomagautomata;

import org.example.repository.CsomagautomataRepository;
import org.example.service.AutomataService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutomataServiceImpl implements AutomataService {

    private final CsomagautomataRepository automataRepository;

    public AutomataServiceImpl(CsomagautomataRepository automataRepository) {
        this.automataRepository = automataRepository;
    }

    @Override
    public Csomagautomata save(Csomagautomata automata) {
        return automataRepository.save(automata);
    }

    @Override
    public Csomagautomata createAutomata(Csomagautomata automata) {
        return automataRepository.save(automata);
    }

    @Override
    public Csomagautomata getAutomataById(Long id) {
        return automataRepository.findById(id).orElse(null);
    }

    @Override
    public Csomagautomata getAutomataByCim(String cim) {
        return automataRepository.findByCim(cim);
    }

    @Override
    public List<Csomagautomata> getAllAutomatak() {
        return automataRepository.findAll();
    }

    @Override
    public void deleteAutomata(Long id) {
        automataRepository.deleteById(id);
    }
}
