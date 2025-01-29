package ru.kata.spring.boot_security.demo.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

   private final RegistrationService registrationService;
   private final UserRepository userRepository;

   @Autowired
   public UserServiceImp(RegistrationService registrationService, UserRepository userRepository) {
       this.registrationService = registrationService;
       this.userRepository = userRepository;
   }

   @Transactional
   @Override
   public void save(User user) {
      registrationService.register(user);
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
   public void update(User user, long id) {
      user.setId(id);
      Optional<User> existingUser = userRepository.findById(id);
      if (user.getPassword() == null || user.getPassword().isEmpty()) {
         user.setPassword(existingUser.get().getPassword());
         userRepository.save(user);
      } else {
         registrationService.register(user);
      }

   }

   @Transactional
   @Override
   public void delete(long id) {
      userRepository.deleteById(id);
   }

}
