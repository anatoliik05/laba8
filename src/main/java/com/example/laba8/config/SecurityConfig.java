package com.example.laba8.config;

import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Для @PreAuthorize
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Отключение CSRF для простоты разработки. В продакшене настройте!
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers("/", "/index", "/register", "/login", "/error").permitAll()
                        // Разрешаем доступ к статическим ресурсам:
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/WEB-INF/jsp/**").denyAll() // JSP файлы должны быть доступны только через контроллеры
                        // Разрешаем доступ к /admin/** только для ROLE_ADMIN
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/index") // Ваша кастомная страница логина
                        .loginProcessingUrl("/authenticateTheUser")
                        .defaultSuccessUrl("/welcome", true) // Всегда перенаправлять на /welcome после успешного входа
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/index") // Перенаправлять на /index после выхода
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // !!! ВНИМАНИЕ: NoOpPasswordEncoder НЕ БЕЗОПАСЕН для продакшн-систем.
        // !!! ЗАМЕНИТЕ на BCryptPasswordEncoder или другой надежный алгоритм.
        // Пример с BCryptPasswordEncoder:
        return new BCryptPasswordEncoder();
        // Если вы еще не используете BCrypt и хотите временно использовать NoOp для тестирования:
        // return NoOpPasswordEncoder.getInstance();
    }
}