package com.example.demo.service;

import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UsernameExistException;
import com.example.demo.models.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User register(String firstName,String lastName,String email,String username) throws UserNotFoundException, UsernameExistException, EmailExistException;
    User register(User user) throws EmailExistException, UsernameExistException, UsernameNotFoundException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
