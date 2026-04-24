package com.example.laba8.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", schema = "users") // Схема "users" сохранена
public class UserEntity implements UserDetails {

    @Id @Getter @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // Integer, как у вас

    @Getter @Setter
    private String name; // Соответствует Username

    @Getter @Setter
    private String password;

    @Getter @Setter
    private String email;

    @Getter @Setter
    private String role = "ROLE_USER"; // Дефолтная роль

    @Getter @Setter
    @Column(nullable = false) // Поле не может быть null
    private boolean deleted = false; // По умолчанию пользователь не удален (active)

    // --- Методы UserDetails ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return List.of(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Мы не используем блокировку аккаунта, но в реальном приложении это может быть связано с полем isDeleted
        // Например: return !this.deleted;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Аккаунт включен, если он не удален
        return !this.deleted;
    }

    // --- Конструкторы (если нужны, но Lombok их не генерирует для всех полей по умолчанию) ---
    // public UserEntity() {}
    //
    // public UserEntity(String name, String password, String email) {
    //     this.name = name;
    //     this.password = password;
    //     this.email = email;
    // }
}