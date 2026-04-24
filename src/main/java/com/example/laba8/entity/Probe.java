package com.example.laba8.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "probes")
public class Probe {
    @Getter @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "substanceName") @Getter @Setter
    private String substanceName;

    @Getter @Setter
    private Double weight;

    // Добавляем поле для мягкого удаления
    @Getter @Setter
    private boolean deleted = false; // По умолчанию проба не удалена

    // Конструкторы (если нужны)
}
