package com.example.laba8.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({"/", "/index"})
    public String index() {
        return "index"; // Возвращает index.jsp
    }

    @GetMapping("/login")
    public String login() {
        return "index"; // Это страница, которая будет отображать форму входа (index.jsp)
    }

    @GetMapping("/access-denied")
    public String showAccessDeniedPage() {
        return "access-denied"; // Страница, когда нет прав доступа
    }
}