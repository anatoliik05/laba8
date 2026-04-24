package com.example.laba8.controller;

import com.example.laba8.entity.Probe;
import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.ProbeRepository;
import com.example.laba8.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/probes")
@PreAuthorize("hasRole('USER')") // Только обычные пользователи имеют доступ к этому контроллеру
public class ProbeController {

    private final ProbeRepository probeRepository;
    private final UserRepository userRepository; // Если нужно привязать пробу к текущему пользователю

    public ProbeController(ProbeRepository probeRepository, UserRepository userRepository) {
        this.probeRepository = probeRepository;
        this.userRepository = userRepository;
    }

    // Показать форму для создания новой пробы
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("probe", new Probe());
        return "probe-form"; // JSP-страница с формой
    }

    // Показать форму для редактирования существующей пробы
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               Authentication authentication, RedirectAttributes redirectAttributes) {
        Optional<Probe> probeOptional = probeRepository.findById(id);
        // Дополнительная проверка, что пользователь имеет право редактировать эту пробу
        if (probeOptional.isPresent() && !probeOptional.get().isDeleted() &&
                isOwner(authentication, probeOptional.get())) { // isOwner - новый метод для проверки
            model.addAttribute("probe", probeOptional.get());
            return "probe-form";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "Проба не найдена, удалена или у вас нет прав на редактирование.");
        return "redirect:/welcome";
    }

    // Сохранить (создать или обновить) пробу
    @PostMapping("/save")
    public String saveProbe(@ModelAttribute Probe probe, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            probeRepository.save(probe);
            redirectAttributes.addFlashAttribute("successMessage", "Проба успешно сохранена!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка при сохранении пробы: " + e.getMessage());
        }
        return "redirect:/welcome";
    }
    // "Мягкое" удаление пробы
    @PostMapping("/delete/{id}")
    public String deleteProbe(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        Optional<Probe> probeOptional = probeRepository.findById(id);
        if (probeOptional.isPresent() && isOwner(authentication, probeOptional.get())) {
            Probe probe = probeOptional.get();
            probe.setDeleted(true);
            probeRepository.save(probe);
            redirectAttributes.addFlashAttribute("successMessage", "Проба успешно удалена.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Проба не найдена или у вас нет прав на удаление.");
        }
        return "redirect:/welcome";
    }

    // Вспомогательный метод для проверки владельца пробы
    private boolean isOwner(Authentication authentication, Probe probe) {
        // Здесь должна быть логика проверки, что probe.getOwner().getUsername().equals(authentication.getName())
        // Пока заглушка:
        return true; // Временная заглушка, в реальном приложении привяжите Probe к User
    }
}