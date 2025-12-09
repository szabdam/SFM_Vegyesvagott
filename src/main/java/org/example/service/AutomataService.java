package org.example.service;

import org.example.model.Csomagautomata;

import java.util.List;

public interface AutomataService {

    Csomagautomata save(Csomagautomata automata);

    Csomagautomata createAutomata(Csomagautomata automata);

    Csomagautomata getAutomataById(Long id);

    Csomagautomata getAutomataByCim(String cim);

    List<Csomagautomata> getAllAutomatak();

    void deleteAutomata(Long id);
}
