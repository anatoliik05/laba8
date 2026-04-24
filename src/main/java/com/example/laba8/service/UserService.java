package com.example.laba8.service; // Убедитесь, что пакет правильный

import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder; // Для хеширования паролей
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserEntity> findAllActiveUsers() {
        return userRepository.findAllByDeletedFalse();
    }

    public List<UserEntity> findAllUsers() { // Для админов, чтобы видеть всех, включая удаленных
        return userRepository.findAll();
    }

    public Optional<UserEntity> findUserById(Integer id) {
        return userRepository.findById(id);
    }

    // --- Добавленные методы для проверки при регистрации ---
    public boolean isNameTaken(String name) {
        return userRepository.findByNameAndDeletedFalse(name).isPresent();
    }

    public boolean isEmailTaken(String email) {
        return userRepository.findByEmailAndDeletedFalse(email).isPresent();
    }
    // --- Конец добавленных методов ---

    @Transactional
    public UserEntity saveUser(UserEntity user) {
        // Если это новый пользователь или пароль был изменен, хешируем его
        // Также проверяем, что пароль не null и не пустой, чтобы избежать хеширования пустой строки
        if (user.getId() == null || (user.getPassword() != null && !user.getPassword().isEmpty() && !user.getPassword().startsWith("$2a$"))) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else if (user.getId() != null) {
            // Если пароль не меняется при редактировании, восстанавливаем старый
            userRepository.findById(user.getId()).ifPresent(existingUser -> {
                if (user.getPassword() == null || user.getPassword().isEmpty()) { // если пароль в форме пустой
                    user.setPassword(existingUser.getPassword());
                }
            });
        }
        return userRepository.save(user);
    }

    @Transactional
    public void softDeleteUser(Integer id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setDeleted(true);
            userRepository.save(user);
        });
    }

    @Transactional
    public void restoreUser(Integer id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setDeleted(false);
            userRepository.save(user);
        });
    }

    @Transactional
    public void makeAdmin(Integer id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole("ROLE_ADMIN");
            userRepository.save(user);
        });
    }

    @Transactional
    public void makeUser(Integer id) {
        userRepository.findById(id).ifPresent(user -> {
            user.setRole("ROLE_USER");
            userRepository.save(user);
        });
    }
}