package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.util.NoSuchUserException;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adminpanel")
public class AdminRestController {

    private final UserService userService;
    private final RoleService roleService;

    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public List<UserDTO> showAllUsers() {
        return userService.listUsers().stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/roles")
    public Set<Role> getAllRoles() {
        return roleService.findAll();
    }

    @GetMapping("/users/{id}")
    public UserDTO showUser(@PathVariable long id) {
        User user = userService.show(id);
        if(user==null) {
            throw new NoSuchUserException("DT don't have user with id: " + id);
        }
        return convertToUserDTO(user);
    }

    @PostMapping("/users")
    public ResponseEntity<?> addNewUser(@RequestBody @Valid UserDTO userDTO,
                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            // Собираем сообщения об ошибках
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors); // Возвращаем ошибки в формате JSON
        }

        Set<Role> roles = new HashSet<>();
        for (Role role : userDTO.getRoles()) {
            Role existingRole = roleService.findByRole(role.getRole());
            if (existingRole == null) {
                throw new RuntimeException("Role not found: " + role.getRole());
            }
            roles.add(existingRole);
        }
        userDTO.setRoles(roles);

        userService.save(convetedToUser(userDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PutMapping("/users")
    public ResponseEntity<?> updateUser(@RequestBody @Valid UserDTO userDTO,
                           BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Set<Role> roles = new HashSet<>();
        for (Role role : userDTO.getRoles()) {
            Role existingRole = roleService.findByRole(role.getRole());
            if (existingRole == null) {
                throw new RuntimeException("Role not found: " + role.getRole());
            }
            roles.add(existingRole);
        }

        userDTO.setRoles(roles);
        userService.update(convetedToUser(userDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable long id) {
        User user = userService.show(id);
        if(user==null) {
            throw new NoSuchUserException("DT don't have user with id: " + id);
        }
        userService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private User convetedToUser(UserDTO userDTO) {
        return new ModelMapper().map(userDTO, User.class);
    }

    private UserDTO convertToUserDTO (User user) {
        return new ModelMapper().map(user, UserDTO.class);
    }

}
