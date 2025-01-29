package ru.kata.spring.boot_security.demo.model;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "id")
   private Long id;


   @Column(name = "name")
   private String name;

   @Column(name = "password")
   private String password;

   @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
   @JoinTable(
           name = "users_roles",
           joinColumns = @JoinColumn(name = "user_id"),
           inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<Role> roles;

   public User() {
   }

   public User(String name, String password, String role) {
      this.name = name;
      this.password = password;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

      public Set<Role> getRoles() {
         return roles;
      }

      public void setRoles(Set<Role> roles) {
         this.roles = roles;
      }
}
