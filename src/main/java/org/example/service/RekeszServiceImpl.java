package org.example.service;

import org.example.model.Rekesz;
import org.example.repository.RekeszRepository;
import org.example.service.RekeszService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RekeszServiceImpl implements RekeszService {

    private final RekeszRepository rekeszRepository;

    public RekeszServiceImpl(RekeszRepository rekeszRepository) {
        this.rekeszRepository = rekeszRepository;
    }

    @Override
    public Rekesz createRekesz(Rekesz rekesz) {
        return rekeszRepository.save(rekesz);
    }

    @Override
    public Rekesz getRekeszById(Long id) {
        return rekeszRepository.findById(id).orElse(null);
    }

    @Override
    public List<Rekesz> getRekeszekByAutomata(Long automataId) {
        return rekeszRepository.findByAutomataId(automataId);
    }

    @Override
    public List<Rekesz> getSzabadRekeszek(Long automataId) {
        return rekeszRepository.findByAutomataIdAndFoglaltFalse(automataId);
    }

    @Override
    public Rekesz save(Rekesz rekesz) {
        return rekeszRepository.save(rekesz);
    }
}
