package com.example.laba8.restcontroller; // Убедитесь, что пакет корректен

import com.example.laba8.entity.Experiment;
import com.example.laba8.entity.Probe;
import com.example.laba8.entity.Team;
import com.example.laba8.repository.ExperimentRepository;
import com.example.laba8.repository.ProbeRepository;
import com.example.laba8.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/experiments")
public class ExperimentRestController {

    @Autowired
    private ExperimentRepository experimentRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private ProbeRepository probeRepository;

    // Доступно USER и ADMIN
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Experiment> getAllExperiments() {
        // Вызываем правильный метод: findAllActiveWithDetails()
        return experimentRepository.findAllActiveWithDetails();
    }

    // Доступно USER и ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Experiment> getExperimentById(@PathVariable Long id) {
        return experimentRepository.findById(id)
                .map(experiment -> new ResponseEntity<>(experiment, HttpStatus.OK))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Experiment>build());
    }

    // Доступно только ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Experiment> createExperiment(@RequestBody Experiment experiment) {
        // Проверка существования team и probe (исправлено условие)
        if (experiment.getTeam() == null || experiment.getTeam().getId() == null || !teamRepository.existsById(experiment.getTeam().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).<Experiment>build();
        }
        if (experiment.getProbe() == null || experiment.getProbe().getId() == null || !probeRepository.existsById(experiment.getProbe().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).<Experiment>build();
        }
        experiment.setId(null); // ID должен генерироваться базой данных
        Experiment savedExperiment = experimentRepository.save(experiment);
        return new ResponseEntity<>(savedExperiment, HttpStatus.CREATED);
    }

    // Доступно только ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Experiment> updateExperiment(@PathVariable Long id, @RequestBody Experiment experimentDetails) {
        return experimentRepository.findById(id)
                .map(existingExperiment -> {
                    existingExperiment.setResult(experimentDetails.getResult());

                    // Обновление Team (исправлено условие)
                    if (experimentDetails.getTeam() != null && experimentDetails.getTeam().getId() != null) {
                        Optional<Team> newTeam = teamRepository.findById(experimentDetails.getTeam().getId());
                        if (newTeam.isPresent()) {
                            existingExperiment.setTeam(newTeam.get());
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).<Experiment>build();
                        }
                    } else if (experimentDetails.getTeam() == null) {
                        existingExperiment.setTeam(null); // Разрешить установить team в null
                    }

                    // Обновление Probe (исправлено условие)
                    if (experimentDetails.getProbe() != null && experimentDetails.getProbe().getId() != null) {
                        Optional<Probe> newProbe = probeRepository.findById(experimentDetails.getProbe().getId());
                        if (newProbe.isPresent()) {
                            existingExperiment.setProbe(newProbe.get());
                        } else {
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).<Experiment>build();
                        }
                    } else if (experimentDetails.getProbe() == null) {
                        existingExperiment.setProbe(null); // Разрешить установить probe в null
                    }

                    Experiment updatedExperiment = experimentRepository.save(existingExperiment);
                    return new ResponseEntity<>(updatedExperiment, HttpStatus.OK);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Experiment>build());
    }

    // Доступно только ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteExperiment(@PathVariable Long id) {
        try {
            experimentRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // В случае ошибки удаления (например, связанные данные)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
        }
    }
}