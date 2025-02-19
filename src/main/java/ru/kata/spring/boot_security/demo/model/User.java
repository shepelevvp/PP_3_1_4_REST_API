package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;

   @NotEmpty(message = "Имя не должно быть пустым")
   @Size(min = 2, max= 50, message = "Имя должно быть от 2 до 50 символов")
   @Column(name = "name")
   private String name;

   @Column(name = "lastname")
   private String lastname;

   @Column(name = "password")
   private String password;

   @Column(name = "age")
   @Min(value = 0, message = "Возраст не может быть отрицательным")
   private int age;

   @NotEmpty(message = "Поле Email не должно быть пустым")
   @Email(message = "Это поле должно быть как email")
   @Column(name = "email", unique = true)
   private String email;

   @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> roles = new HashSet<>();

   public User() {
   }

   @Override
   public Collection<? extends GrantedAuthority> getAuthorities() {
      List<SimpleGrantedAuthority> collect = this.getRoles().stream()
              .map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
      System.out.println(collect);
      return collect;
   }

   @Override
   public String getPassword() {
      return password;
   }

   @Override
   public String getUsername() {
      return email;
   }

   @Override
   public boolean isAccountNonExpired() {
      return true;
   }

   @Override
   public boolean isAccountNonLocked() {
      return true;
   }

   @Override
   public boolean isCredentialsNonExpired() {
      return true;
   }

   @Override
   public boolean isEnabled() {
      return true;
   }
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getLastname() {
      return lastname;
   }

   public void setLastname(String lastname) {
      this.lastname = lastname;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public int getAge() {
      return age;
   }

   public void setAge(int age) {
      this.age = age;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public boolean hasRole(String role) {
      for (Role r : roles) {
         if (r.getRole().equals(role)) {
            return true;
         }
      }
      return false;
   }

   @Override
   public String toString() {
      return "User{" +
              "name='" + name + '\'' +
              ", password='" + password + '\'' +
              ", age=" + age +
              ", email='" + email + '\'' +
              ", roles=" + roles +
              '}';
   }
}
