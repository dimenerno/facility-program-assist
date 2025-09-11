package com.facilityassist.repository;

import com.facilityassist.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {
    Optional<Unit> findByName(String name);
    Optional<Unit> findByCode(String code);
    boolean existsByName(String name);
    boolean existsByCode(String code);
}
