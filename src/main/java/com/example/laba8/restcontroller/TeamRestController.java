package com.example.laba8.restcontroller;

import com.example.laba8.entity.Team;
import com.example.laba8.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
public class TeamRestController {
    @Autowired
    private TeamRepository teamRepository;

    // Доступно USER и ADMIN
    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    // Доступно USER и ADMIN
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        return teamRepository.findById(id)
                .map(team -> new ResponseEntity<>(team, HttpStatus.OK))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Team>build());
    }

    // Доступно только ADMIN
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        team.setId(null); // ID должен генерироваться базой данных
        Team savedTeam = teamRepository.save(team);
        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
    }

    // Доступно только ADMIN
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team teamDetails) {
        return teamRepository.findById(id)
                .map(existingTeam -> {
                    existingTeam.setTeamName(teamDetails.getTeamName());
                    Team updatedTeam = teamRepository.save(existingTeam);
                    return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
                })
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).<Team>build());
    }

    // Доступно только ADMIN
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        try {
            teamRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            // В случае ошибки удаления (например, связанные данные)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build();
        }
    }
}
