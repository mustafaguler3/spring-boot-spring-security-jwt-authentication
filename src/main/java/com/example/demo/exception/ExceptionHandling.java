package com.example.demo.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.payload.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandling{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. if this is an error, contact" + "adminitration";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occured while processing the request";

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<HttpResponse> emailExistsException(EmailNotFoundException ex){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(300);
        httpResponse.setReason(ex.getLocalizedMessage().toUpperCase());
        httpResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(httpResponse,HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(DisabledException ex){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(403);
        httpResponse.setReason(ex.getLocalizedMessage().toUpperCase());
        httpResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(httpResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException ex){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(404);
        httpResponse.setReason(ex.getLocalizedMessage().toUpperCase());
        httpResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(httpResponse,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException ex){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(300);
        httpResponse.setReason(ex.getLocalizedMessage().toUpperCase());
        httpResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(httpResponse,HttpStatus.BAD_GATEWAY);
    }
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpResponse> usernameNotFoundException(UsernameNotFoundException ex){
        HttpResponse httpResponse = new HttpResponse();
        httpResponse.setStatusCode(404);
        httpResponse.setReason(ex.getLocalizedMessage().toUpperCase());
        httpResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(httpResponse,HttpStatus.NOT_FOUND);
    }
}



















