package com.example.demo.controller;

import com.example.demo.exception.EmailExistException;
import com.example.demo.exception.ExceptionHandling;
import com.example.demo.exception.UsernameExistException;
import com.example.demo.models.User;
import com.example.demo.security.UserDetailsImpl;
import com.example.demo.security.jwt.JwtTokenProvider;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.demo.constants.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(path = "/user")
public class UserController extends ExceptionHandling {

    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user){
        authenticate(user.getUsername(),user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserDetailsImpl userPrincipal = new UserDetailsImpl(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser,jwtHeader,OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) throws Exception {
        //User newUser = userService.register(user);
        User newUser = userService.register(user.getFirstName(),user.getLastName(),user.getEmail(),user.getUsername());
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    private HttpHeaders getJwtHeader(UserDetailsImpl user){
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER,jwtTokenProvider.generateJwtToken(user));
        return headers;
    }

    private void authenticate(String username,String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
    }
}
























