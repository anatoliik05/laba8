package com.example.laba8.controller;

import com.example.laba8.repository.ExperimentRepository;
import com.example.laba8.repository.ProbeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

    private final ExperimentRepository experimentRepo;
    private final ProbeRepository probeRepo;

    public WelcomeController(ExperimentRepository experimentRepo, ProbeRepository probeRepo) {
        this.experimentRepo = experimentRepo;
        this.probeRepo = probeRepo;
    }

    @GetMapping("/welcome")
    public String welcome(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login"; // Перенаправление на страницу логина, если пользователь не аутентифицирован
        }

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            // Для админа показываем все активные эксперименты
            // Предполагается, что в ExperimentRepository есть метод findAllActiveWithDetails()
            model.addAttribute("dataList", experimentRepo.findAllActiveWithDetails());
        } else {
            // Для обычного пользователя показываем только его активные пробы
            // (Нужно будет изменить findAll() на findAllActive() и, возможно, фильтровать по пользователю)
            // Пока оставляю как было, но в реальном приложении User должен быть связан с Probe
            model.addAttribute("dataList", probeRepo.findAllActive()); // Предполагается, что в ProbeRepository есть метод findAllActive()
        }

        // username уже доступен через security taglib в JSP, но можно добавить и так
        model.addAttribute("username", authentication.getName());

        return "welcome";
    }
}