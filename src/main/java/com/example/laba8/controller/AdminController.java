package com.example.laba8.controller;

import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.UserRepository;
import com.example.laba8.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping({"/admin", "/users"}) // Обрабатываем оба варианта путей
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // --- READ (Список пользователей) ---
    // Обрабатывает: /admin/users, /users/manage и просто /admin/
    @GetMapping({"/users", "/manage", "/"})
    public String listUsers(Model model) {
        List<UserEntity> users = userService.findAllUsers();
        model.addAttribute("users", users);
        // Возвращаем существующий файл /WEB-INF/jsp/admin.jsp
        return "admin";
    }

    // --- CREATE (Форма) ---
    @GetMapping("/users/new")
    public String showCreateUserForm(Model model) {
        model.addAttribute("user", new UserEntity());
        model.addAttribute("isNewUser", true);
        // Возвращаем существующий файл /WEB-INF/jsp/admin/user-form.jsp
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute UserEntity user, RedirectAttributes redirectAttributes) {
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь '" + user.getName() + "' успешно создан!");
        return "redirect:/admin/users";
    }

    // --- UPDATE (Форма редактирования) ---
    @GetMapping("/users/{id}/edit")
    public String showEditUserForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<UserEntity> userOptional = userService.findUserById(id);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            model.addAttribute("isNewUser", false);
            return "admin/user-form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Пользователь с ID " + id + " не найден.");
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/users/{id}")
    public String updateUser(@PathVariable Integer id, @ModelAttribute UserEntity user, RedirectAttributes redirectAttributes) {
        user.setId(id);
        userService.saveUser(user);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь успешно обновлен!");
        return "redirect:/admin/users";
    }

    // --- SOFT DELETE ---
    @PostMapping("/users/{id}/delete")
    public String softDeleteUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.softDeleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь с ID " + id + " помечен как удаленный.");
        return "redirect:/admin/users";
    }

    // --- RESTORE ---
    @PostMapping("/users/{id}/restore")
    public String restoreUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.restoreUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь с ID " + id + " успешно восстановлен.");
        return "redirect:/admin/users";
    }

    // --- MAKE ADMIN ---
    @PostMapping("/users/{id}/make-admin")
    public String makeAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.makeAdmin(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь с ID " + id + " назначен админом.");
        return "redirect:/admin/users";
    }

    // --- MAKE USER ---
    @PostMapping("/users/{id}/make-user")
    public String makeUser(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        userService.makeUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "Пользователь с ID " + id + " понижен до обычного пользователя.");
        return "redirect:/admin/users";
    }
}