package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;

import javax.validation.constraints.*;
import java.util.Set;

public class UserDTO {

    private Long id;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max= 50, message = "Имя должно быть от 2 до 50 символов")
    private String name;

    private String lastname;

    private String password;

    @Min(value = 0, message = "Возраст не может быть отрицательным")
    private int age;

    @NotEmpty(message = "Поле Email не должно быть пустым")
    @Email(message = "Это поле должно быть как email")
    private String email;

    private Set<Role> roles;

    // Геттер для поля id
    public Long getId() {
        return id;
    }

    // Сеттер для поля id
    public void setId(Long id) {
        this.id = id;
    }

    // Геттер для поля name
    public String getName() {
        return name;
    }

    // Сеттер для поля name
    public void setName(String name) {
        this.name = name;
    }

    // Геттер для поля lastname
    public String getLastname() {
        return lastname;
    }

    // Сеттер для поля lastname
    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    // Геттер для поля password
    public String getPassword() {
        return password;
    }

    // Сеттер для поля password
    public void setPassword(String password) {
        this.password = password;
    }

    // Геттер для поля age
    public int getAge() {
        return age;
    }

    // Сеттер для поля age
    public void setAge(int age) {
        this.age = age;
    }

    // Геттер для поля email
    public String getEmail() {
        return email;
    }

    // Сеттер для поля email
    public void setEmail(String email) {
        this.email = email;
    }

    // Геттер для поля roles
    public Set<Role> getRoles() {
        return roles;
    }

    // Сеттер для поля roles
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
