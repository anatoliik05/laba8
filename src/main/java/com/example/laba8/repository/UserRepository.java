package com.example.laba8.repository;

import com.example.laba8.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> { // Integer, как у вас
    // Находит пользователя по имени, который НЕ удален
    Optional<UserEntity> findByNameAndDeletedFalse(String name);

    // Находит пользователя по email, который НЕ удален
    Optional<UserEntity> findByEmailAndDeletedFalse(String email);

    // Возвращает список всех НЕ удаленных пользователей
    List<UserEntity> findAllByDeletedFalse();

    // findAll() из JpaRepository вернет всех пользователей, включая удаленных.
    // Если хотите findById, который игнорирует удаленных:
    Optional<UserEntity> findByIdAndDeletedFalse(Integer id); // Integer, как у вас
}