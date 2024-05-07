package com.example.demo.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.demo.payload.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
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
        return createHttpResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException(EmailNotFoundException ex){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ACCOUNT_DISABLED);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<HttpResponse> tokenExpiredException(TokenExpiredException ex){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    @ExceptionHandler(UsernameExistException.class)
    public ResponseEntity<HttpResponse> usernameExistException(UsernameExistException ex){
        return createHttpResponse(HttpStatus.BAD_REQUEST,ex.getMessage());
    }

    private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus status,String message){
        return new ResponseEntity<>(new HttpResponse(status.value(),status,status.getReasonPhrase(),message.toUpperCase()),status);
    }
}



















