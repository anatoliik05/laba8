package com.example.laba8.config;

import com.example.laba8.entity.Experiment;
import com.example.laba8.entity.Probe;
import com.example.laba8.entity.Team;
import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.ExperimentRepository;
import com.example.laba8.repository.ProbeRepository;
import com.example.laba8.repository.TeamRepository;
import com.example.laba8.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    // Добавьте ваши репозитории здесь
    private final TeamRepository teamRepository;
    private final ProbeRepository probeRepository;
    private final ExperimentRepository experimentRepository;

    public DataLoader(UserRepository userRepository,
                      PasswordEncoder passwordEncoder,
                      TeamRepository teamRepository,
                      ProbeRepository probeRepository,
                      ExperimentRepository experimentRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamRepository = teamRepository;
        this.probeRepository = probeRepository;
        this.experimentRepository = experimentRepository;
    }

    @Override
    @Transactional // Важно для корректного сохранения связей
    public void run(String... args) throws Exception {

        // 1. Создание админа (ваш код)
        if (userRepository.findByNameAndDeletedFalse("admin").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setName("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRole("ROLE_ADMIN");
            admin.setDeleted(false);
            userRepository.save(admin);
            System.out.println("--- Initial admin user created ---");
        }

        // 2. Добавляем бригады (Teams)
        if (teamRepository.count() == 0) {
            Team alpha = new Team();
            alpha.setTeamName("Бригада Альфа");
            teamRepository.save(alpha);

            Team beta = new Team();
            beta.setTeamName("Бригада Бета");
            teamRepository.save(beta);

            // 3. Добавляем пробы (Probes)
            Probe h2so4 = new Probe();
            h2so4.setSubstanceName("H2SO4 (Серная кислота)");
            h2so4.setWeight(1.5);
            probeRepository.save(h2so4);

            Probe nacl = new Probe();
            nacl.setSubstanceName("NaCl (Соль)");
            nacl.setWeight(0.5);
            probeRepository.save(nacl);

            // 4. Добавляем эксперименты (связи)
            Experiment exp1 = new Experiment();
            exp1.setTeam(alpha); // Используем объекты, а не ID
            exp1.setProbe(h2so4);
            exp1.setResult("Успешно: реакция прошла штатно");
            experimentRepository.save(exp1);

            Experiment exp2 = new Experiment();
            exp2.setTeam(beta);
            exp2.setProbe(nacl);
            exp2.setResult("В процессе: ожидание осадка");
            experimentRepository.save(exp2);

            System.out.println("--- Test teams, probes and experiments loaded ---");
        }
    }
}