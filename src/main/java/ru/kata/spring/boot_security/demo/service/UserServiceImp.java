package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

   private final PasswordEncoder passwordEncoder;
   private final UserRepository userRepository;

   @Autowired
   public UserServiceImp(PasswordEncoder passwordEncoder, UserRepository userRepository) {
       this.passwordEncoder = passwordEncoder;
       this.userRepository = userRepository;
   }

   @Transactional
   @Override
   public void save(User user) {
      this.register(user);
   }

   @Transactional(readOnly = true)
   @Override
   public List<User> listUsers() {
      return userRepository.findAll();
   }

   @Transactional(readOnly = true)
   @Override
   public User show(Long id) {
      Optional<User> foundUser =  userRepository.findById(id);
      return foundUser.orElse(null);
   }

   @Transactional
   @Override
   public void update(User user) {
      if (user.getPassword() == null || user.getPassword().isEmpty()) {
         Optional<User> existingUser = userRepository.findById(user.getId());
         user.setPassword(existingUser.get().getPassword());
         userRepository.save(user);
      } else {
         this.register(user);
      }
   }

   @Transactional
   @Override
   public void delete(long id) {
      userRepository.deleteById(id);
   }

   @Transactional(readOnly = true)
   @Override
   public User findByName(String name) {
      Optional<User> foundUser = userRepository.findByName(name);
      return foundUser.orElse(null);
   }

   @Transactional
   public void register(User user) {
      user.setPassword(passwordEncoder.encode(user.getPassword()));
      userRepository.save(user);
   }

   @Transactional
   public User findByEmail(String email) {
      return userRepository.findByEmail(email).orElse(null);
   }


}
