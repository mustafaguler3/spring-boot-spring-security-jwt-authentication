package com.example.demo.controller;

import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.ExceptionHandling;
import com.example.demo.exception.UsernameExistException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/user")
@RestController
public class TestController extends ExceptionHandling {

    @GetMapping("/home")
    public String showUser() throws EmailExistException, UsernameExistException {
        throw new UsernameExistException("This username is already taken");
    }


}
















