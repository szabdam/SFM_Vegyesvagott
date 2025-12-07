package org.example.repository;

import org.example.model.Csomagautomata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsomagautomataRepository extends JpaRepository<Csomagautomata, Long> {
    Optional<Csomagautomata> findByCim(String cim);
}
