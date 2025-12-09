package org.example.service;

import org.example.model.Csomag;
import java.util.List;

public interface CsomagService {

    Csomag getByAzonosito(Long id);

    boolean isValidID(String id);

    String getAutomataNev(String id);

    Csomag saveCsomag(Csomag csomag);

    Csomag getCsomagById(Long id);

    List<Csomag> getAllCsomagok();

    void deleteCsomag(Long id);
}
