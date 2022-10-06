package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;

import java.util.List;


@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{}")
    public String getUsers(  Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin";
    }

    @GetMapping("/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "add_user";
    }

    @PostMapping("/add") // Map ONLY POST Requests
    public String addUser(@ModelAttribute("user") User user) {
        user.setActive(true);
        userRepository.save(user);
        return redirect();
    }

    @GetMapping("/edit/{id}")
    public String updateUser(Model model, @PathVariable("id") long id){
        model.addAttribute("user", userRepository.findById(id));
        return "edit_user";
    }

    @PatchMapping ("/edit")
    public String updateUserPost(@ModelAttribute("user") User user) {
        user.setActive(true);
        userRepository.save(user);
        return redirect();

    }




    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") long id) {
       userRepository.deleteById(id);
        return redirect();
    }


    private String redirect() {
        User admin = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        return "redirect:/admin/" + admin.getId();
    }
}
