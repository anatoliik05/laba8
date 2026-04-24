package com.example.laba8.controller;

import com.example.laba8.entity.Experiment;
import com.example.laba8.repository.ExperimentRepository;
import com.example.laba8.repository.ProbeRepository;
import com.example.laba8.repository.TeamRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/experiments")
@PreAuthorize("hasRole('ADMIN')") // Только администраторы имеют доступ к этому контроллеру
public class ExperimentController {
    private final ExperimentRepository experimentRepository;
    private final TeamRepository teamRepository; // Предполагаем, что у вас есть TeamRepository
    private final ProbeRepository probeRepository; // Предполагаем, что у вас есть ProbeRepository

    public ExperimentController(ExperimentRepository experimentRepository,
                                TeamRepository teamRepository,
                                ProbeRepository probeRepository) {
        this.experimentRepository = experimentRepository;
        this.teamRepository = teamRepository;
        this.probeRepository = probeRepository;
    }

    // Показать форму для создания нового эксперимента
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("experiment", new Experiment());
        model.addAttribute("teams", teamRepository.findAll()); // Загружаем все бригады для выпадающего списка
        model.addAttribute("probes", probeRepository.findAllActive()); // Загружаем все активные пробы
        return "experiment-form"; // JSP-страница с формой
    }

    // Показать форму для редактирования существующего эксперимента
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Experiment> experimentOptional = experimentRepository.findById(id);
        if (experimentOptional.isPresent() && !experimentOptional.get().isDeleted()) {
            model.addAttribute("experiment", experimentOptional.get());
            model.addAttribute("teams", teamRepository.findAll());
            model.addAttribute("probes", probeRepository.findAllActive());
            return "experiment-form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Эксперимент не найден или удален.");
        return "redirect:/welcome"; // Перенаправляем на главную, если эксперимент не найден
    }

    // Сохранить (создать или обновить) эксперимент
    @PostMapping("/save")
    public String saveExperiment(@ModelAttribute Experiment experiment, RedirectAttributes redirectAttributes) {
        // Здесь можно добавить валидацию
        try {
            // Если experiment.id == null, это новая запись, иначе - обновление
            // Важно: если в форме только ID команды и пробы, то нужно их загрузить из репозиториев
            // Например:
            // Team team = teamRepository.findById(experiment.getTeam().getId()).orElseThrow(() -> new RuntimeException("Team not found"));
            // Probe probe = probeRepository.findById(experiment.getProbe().getId()).orElseThrow(() -> new RuntimeException("Probe not found"));
            // experiment.setTeam(team);
            // experiment.setProbe(probe);

            experimentRepository.save(experiment);
            redirectAttributes.addFlashAttribute("successMessage", "Эксперимент успешно сохранен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении эксперимента: " + e.getMessage());
        }
        return "redirect:/welcome"; // После сохранения возвращаемся на главную
    }

    // "Мягкое" удаление эксперимента (установка флага deleted)
    @PostMapping("/delete/{id}")
    public String deleteExperiment(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Experiment> experimentOptional = experimentRepository.findById(id);
        if (experimentOptional.isPresent()) {
            Experiment experiment = experimentOptional.get();
            experiment.setDeleted(true); // Устанавливаем флаг "удален"
            experimentRepository.save(experiment);
            redirectAttributes.addFlashAttribute("successMessage", "Эксперимент успешно удален.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Эксперимент не найден.");
        }
        return "redirect:/welcome";
    }
}
