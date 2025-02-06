package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/adminpanel")
public class AdminPanelController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminPanelController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String viewAdminPanel(Principal principal,
                                 ModelMap model) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("userauth", userService.findByEmail(principal.getName()));
        model.addAttribute("allRoles", roleService.findAll());
        model.addAttribute("editUser", new User());
        model.addAttribute("newUser", new User());
        model.addAttribute("Role", new Role());
        return "adminpanel";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("editUser") User user) {
        userService.update(user);
        return "redirect:/adminpanel";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/adminpanel";
    }

}
