package org.example.repository;

import org.example.model.Rekesz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RekeszRepository extends JpaRepository<Rekesz, Long> {

}
