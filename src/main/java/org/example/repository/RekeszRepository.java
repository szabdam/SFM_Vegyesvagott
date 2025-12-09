package org.example.repository;

import org.example.model.Csomagautomata;
import org.example.model.Rekesz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RekeszRepository extends JpaRepository<Rekesz, Long> {
    Optional<Rekesz> findFirstByAutomataAndMeretAndFoglaltFalse(Csomagautomata automata, String meret);
}
