package com.example.laba8.restcontroller; // Убедитесь, что пакет соответствует вашему проекту

import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users") // Базовый путь для всех запросов к пользователям
public class UserRestController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Внедряем PasswordEncoder для хеширования паролей

    // GET /api/users - Получить список всех пользователей
    // Только ADMIN может просматривать список всех пользователей
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    // GET /api/users/{id} - Получить пользователя по ID
    // Только ADMIN может просматривать любого пользователя по ID
    // Пользователь также может просмотреть свой собственный профиль (дополнительная логика, если нужно)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Integer id) {
        Optional<UserEntity> user = userRepository.findById(id);
        return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // POST /api/users - Создать нового пользователя
    // Только ADMIN может создавать пользователей через API (регистрация через /register доступна всем)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        // Убедимся, что ID не установлен клиентом, чтобы база данных его сгенерировала
        user.setId(null); // Для autoincrement ID лучше передавать null, а не 0 для Integer
        // Хешируем пароль перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // Устанавливаем роль по умолчанию, если не указана или если ADMIN ее не изменил
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER"); // Убедитесь, что роль начинается с "ROLE_"
        }
        UserEntity savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    // PUT /api/users/{id} - Обновить существующего пользователя
    // Только ADMIN может обновлять любого пользователя
    // (пользователь может обновлять себя, но это требует более сложной логики PreAuthorize и проверки прав)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Integer id, @RequestBody UserEntity userDetails) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            UserEntity existingUser = userOptional.get();
            existingUser.setName(userDetails.getName());
            existingUser.setEmail(userDetails.getEmail());

            // Обновляем пароль только если он предоставлен и не пуст, и хешируем его
            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            // Роль также может быть обновлена, если это разрешено (только ADMIN может менять роль)
            // Убедитесь, что роль начинается с "ROLE_"
            if (userDetails.getRole() != null && !userDetails.getRole().isEmpty()) {
                existingUser.setRole(userDetails.getRole());
            }

            UserEntity updatedUser = userRepository.save(existingUser);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        Optional<UserEntity> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found
        }
    }
}