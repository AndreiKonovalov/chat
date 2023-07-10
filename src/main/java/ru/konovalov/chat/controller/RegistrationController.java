package ru.konovalov.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.konovalov.chat.model.Role;
import ru.konovalov.chat.model.User;
import ru.konovalov.chat.repository.UserRepository;
import ru.konovalov.chat.service.UserService;

import java.util.Collections;

@Controller
public class RegistrationController {

    @Autowired
    UserService userService;


    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(User user, Model model) {
        if (!userService.addUser(user)) {
            model.addAttribute("message", true);
            return "registration";
        }
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userService.activateUser(code);
        if (isActivated) {
            model.addAttribute("message", true);
        } else {
            model.addAttribute("message", false);
        }

        return "login";
    }
}
