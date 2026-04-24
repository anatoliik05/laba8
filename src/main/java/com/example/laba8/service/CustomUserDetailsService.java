package com.example.laba8.service;

import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginOrEmail) throws UsernameNotFoundException {
        // Ищем активного пользователя по имени ИЛИ email
        Optional<UserEntity> userByName = userRepository.findByNameAndDeletedFalse(loginOrEmail);
        Optional<UserEntity> userByEmail = userRepository.findByEmailAndDeletedFalse(loginOrEmail);

        UserEntity user = userByName.orElseGet(() -> userByEmail.orElseThrow(
                () -> new UsernameNotFoundException("Пользователь не найден или удален: " + loginOrEmail)
        ));

        // Поскольку UserEntity уже реализует UserDetails, мы можем вернуть его напрямую
        return user;
    }
}
