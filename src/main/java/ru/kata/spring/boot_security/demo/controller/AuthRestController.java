package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/api/authUser")
public class AuthRestController {

    private final UserService userService;

    public AuthRestController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public UserDTO getUserAuth(Principal principal) {
        User authUser = userService.findByEmail(principal.getName());
        return convertToUserDTO(authUser);
    }

    private UserDTO convertToUserDTO (User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }
}
