package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user-list";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUserForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.update(user.getId(), user);
        return "redirect:/admin";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
