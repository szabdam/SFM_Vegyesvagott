package org.example.repository;

import org.example.model.Csomag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CsomagRepository extends JpaRepository<Csomag, Long> {
    Optional<Csomag> findByAzonosito(String atveteliKod);
}
