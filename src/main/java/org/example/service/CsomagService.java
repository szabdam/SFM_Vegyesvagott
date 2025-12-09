package org.example.service;

import org.example.model.Csomag;
import java.util.List;
import java.util.Optional;

public interface CsomagService {

    Optional<Csomag> findByCsomagKod(String kod);

    Csomag getByAzonosito(Long id);

    boolean isValidID(String id);

    String getAutomataNev(String id);

    Csomag saveCsomag(Csomag csomag);

    Csomag getCsomagById(Long id);

    List<Csomag> getAllCsomagok();

    void deleteCsomag(Long id);
}
