package com.example.demo.service.impl;

import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.exception.UsernameExistException;
import com.example.demo.models.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.service.UserService;
import jakarta.transaction.Transactional;
import org.apache.maven.surefire.shared.lang3.RandomStringUtils;
import org.apache.maven.surefire.shared.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Date;
import java.util.List;

import static com.example.demo.constants.Role.ROLE_USER;
import static org.apache.logging.log4j.util.Strings.EMPTY;

@Service
@Transactional
@Qualifier("userDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null){
            throw new UsernameNotFoundException("User not found by username: "+username);
        }else {
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserDetailsImpl userDetails = new UserDetailsImpl(user);
            return userDetails;
        }
    }

    @Override
    public User register(String firstName, String lastName, String email, String username) throws UserNotFoundException, UsernameExistException, EmailExistException {
        User user1 = new User();
        user1.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user1.setFirstName(firstName);
        user1.setLastName(lastName);
        user1.setUsername(username);
        user1.setEmail(email);
        user1.setJoinDate(new Date());
        user1.setPassword(encodedPassword);
        user1.setActive(true);
        user1.setNotLocked(false);
        user1.setRole(ROLE_USER.name());
        user1.setAuthorities(ROLE_USER.getAuthorities());
        user1.setImageUrl(getTemporaryImageUrl());

        userRepository.save(user1);
        return user1;
    }

    @Override
    public User register(User user) throws EmailExistException, UsernameExistException,UsernameNotFoundException {
        //validateNewUsernameAndEmail(user);

            User user1 = new User();
            user1.setUserId(generateUserId());
            String password = generatePassword();
            String encodedPassword = encodePassword(password);
            user1.setFirstName(user.getFirstName());
            user1.setLastName(user.getLastName());
            user1.setUsername(user.getUsername());
            user1.setEmail(user.getEmail());
            user1.setJoinDate(new Date());
            user1.setPassword(encodedPassword);
            user1.setActive(true);
            user1.setNotLocked(false);
            user1.setRole(ROLE_USER.name());
            user1.setAuthorities(ROLE_USER.getAuthorities());
            user1.setImageUrl(getTemporaryImageUrl());

            userRepository.save(user1);
            return user1;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    private String getTemporaryImageUrl(){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
    }
    private String encodePassword(String password){
        return passwordEncoder.encode(password);
    }
    private String generatePassword(){
        return RandomStringUtils.randomAlphabetic(10);
    }
    private String generateUserId(){
        return RandomStringUtils.randomAlphabetic(10);
    }

    private User validateNewUsernameAndEmail(User user) throws EmailExistException, UsernameExistException {

        if (StringUtils.isNotBlank(user.getUsername())){
            User currentUser = findUserByUsername(user.getUsername());
            if (currentUser == null){
                throw new UsernameNotFoundException("No user found by username "+user.getUsername());
            }
            User userByEmail = findUserByUsername(user.getEmail());
            if (userByEmail != null && currentUser.getId().equals(userByEmail.getId())){
                throw new EmailExistException("Email already exists");
            }
            return currentUser;
        }else {
            User userByUsername = findUserByUsername(user.getUsername());
            if (userByUsername != null){
                throw new UsernameExistException("Username already exists");
            }

            User userByEmail = findUserByEmail(user.getEmail());
            if (userByEmail != null){
                throw new EmailExistException("Email already exists");
            }
        }
        return null;
    }
}


















