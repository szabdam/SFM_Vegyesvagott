package org.example.service;

import org.example.model.Rekesz;

import java.util.List;

public interface RekeszService {

    Rekesz createRekesz(Rekesz rekesz);

    Rekesz getRekeszById(Long id);

    List<Rekesz> getRekeszekByAutomata(Long automataId);

    List<Rekesz> getSzabadRekeszek(Long automataId);

    Rekesz save(Rekesz rekesz);
}
