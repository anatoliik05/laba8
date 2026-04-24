package com.example.laba8.repository;

import com.example.laba8.entity.Probe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProbeRepository extends JpaRepository<Probe, Long> {
    // Выбираем только активные (не удаленные) пробы
    @Query("SELECT p FROM Probe p WHERE p.deleted = false")
    List<Probe> findAllActive();

    // Если в будущем вы захотите связать Probe с User и фильтровать по владельцу:
    // @Query("SELECT p FROM Probe p WHERE p.deleted = false AND p.owner.username = ?1")
    // List<Probe> findAllActiveByUser(String username);
}
