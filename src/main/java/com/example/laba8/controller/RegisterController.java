package com.example.laba8.controller;

import com.example.laba8.entity.UserEntity;
import com.example.laba8.repository.UserRepository;
import com.example.laba8.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class RegisterController {

    private final UserService userService; // Инжектируем UserService

    public RegisterController(UserService userService) { // Изменен конструктор
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        // Сообщения теперь приходят через flash attributes, нет нужды в @RequestParam
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam("name") String name,
            @RequestParam("pass") String pass,
            @RequestParam("mail") String mail,
            RedirectAttributes redirectAttributes
    ){
        // Проверяем, заняты ли имя пользователя или email
        if (userService.isNameTaken(name)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Имя пользователя '" + name + "' уже занято. Пожалуйста, выберите другое.");
            return "redirect:/register";
        }
        if (userService.isEmailTaken(mail)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email '" + mail + "' уже используется. Пожалуйста, используйте другой.");
            return "redirect:/register";
        }

        try {
            UserEntity newUser = new UserEntity();
            newUser.setName(name);
            newUser.setPassword(pass); // Пароль будет хеширован в UserService
            newUser.setEmail(mail);
            newUser.setRole("ROLE_USER");
            newUser.setDeleted(false); // Убедимся, что по умолчанию не удален

            userService.saveUser(newUser); // Сохраняем пользователя через UserService

            redirectAttributes.addFlashAttribute("successMessage", "Аккаунт успешно создан! Теперь вы можете войти.");
            return "redirect:/index"; // Перенаправляем на страницу входа
        } catch (Exception e) {
            // В случае любой другой ошибки
            redirectAttributes.addFlashAttribute("errorMessage", "Произошла ошибка при регистрации. Пожалуйста, попробуйте еще раз.");
            // Логирование ошибки: logger.error("Ошибка при регистрации пользователя: {}", e.getMessage(), e);
            return "redirect:/register";
        }
    }
}
