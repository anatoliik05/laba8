package com.example.laba8.restcontroller;

import com.example.laba8.entity.Probe;
import com.example.laba8.repository.ProbeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/probes")
public class ProbeRestController {
    @Autowired
    private ProbeRepository probeRepository;

    // Доступно USER и ADMIN
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Probe> getAllProbes() {
        return probeRepository.findAll();
    }

    // Доступно USER и ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Probe> getProbeById(@PathVariable Long id) {
        return probeRepository.findById(id)
                .map(probe -> new ResponseEntity<>(probe, HttpStatus.OK))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Probe>build());
    }

    // Доступно только ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Probe> createProbe(@RequestBody Probe probe) {
        probe.setId(null); // ID должен генерироваться базой данных
        Probe savedProbe = probeRepository.save(probe);
        return new ResponseEntity<>(savedProbe, HttpStatus.CREATED);
    }

    // Доступно только ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Probe> updateProbe(@PathVariable Long id, @RequestBody Probe probeDetails) {
        return probeRepository.findById(id)
                .map(existingProbe -> {
                    existingProbe.setSubstanceName(probeDetails.getSubstanceName());
                    existingProbe.setWeight(probeDetails.getWeight());
                    Probe updatedProbe = probeRepository.save(existingProbe);
                    return new ResponseEntity<>(updatedProbe, HttpStatus.OK);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Probe>build());
    }

    // Доступно только ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProbe(@PathVariable Long id) {
        try {
            probeRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // В случае ошибки удаления (например, связанные данные)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
        }
    }
}