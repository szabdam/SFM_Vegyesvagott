package org.example.repository;

import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RekeszRepository extends JpaRepository<Rekesz, Long> {

    // Keresés automata ID alapján
    List<Rekesz> findByAutomataId(Long automataId);

    // Szabad rekeszek keresése egy adott automatában
    List<Rekesz> findByAutomataIdAndFoglaltFalse(Long automataId);
}
