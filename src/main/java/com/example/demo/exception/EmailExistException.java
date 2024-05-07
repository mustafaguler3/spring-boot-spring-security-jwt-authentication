package com.example.demo.exception;

public class EmailExistException extends Exception{
    public EmailExistException(String message) {
        super(message);
    }
}
