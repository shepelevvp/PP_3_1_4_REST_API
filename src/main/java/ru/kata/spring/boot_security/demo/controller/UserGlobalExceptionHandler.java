package ru.kata.spring.boot_security.demo.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.kata.spring.boot_security.demo.util.NoSuchUserException;
import ru.kata.spring.boot_security.demo.util.UserIncorrectDate;
import ru.kata.spring.boot_security.demo.util.UserNotCreatedException;

@ControllerAdvice
public class UserGlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<UserIncorrectDate> handlerException(NoSuchUserException exception) {

        UserIncorrectDate response = new UserIncorrectDate(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectDate> handlerException(UserNotCreatedException exception) {

        UserIncorrectDate response = new UserIncorrectDate(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserIncorrectDate> handlerException(Exception exception) {

        UserIncorrectDate response = new UserIncorrectDate(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
