package com.example.laba8.repository;

import com.example.laba8.entity.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
    // Выбираем только активные (не удаленные) эксперименты с деталями команды и пробы
    @Query("SELECT e FROM Experiment e JOIN FETCH e.team JOIN FETCH e.probe WHERE e.deleted = false")
    List<Experiment> findAllActiveWithDetails();
}
