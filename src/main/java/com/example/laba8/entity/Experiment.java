package com.example.laba8.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "experiments")
public class Experiment {
    @Getter @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // Указываем LAZY для производительности
    @Getter @Setter
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY) // Указываем LAZY для производительности
    @Getter @Setter
    @JoinColumn(name = "probe_id")
    private Probe probe;

    @Getter @Setter
    private String result;

    // Добавляем поле для мягкого удаления
    @Getter @Setter
    private boolean deleted = false; // По умолчанию эксперимент не удален

    // Конструкторы (если нужны, Lombok @NoArgsConstructor, @AllArgsConstructor)
}