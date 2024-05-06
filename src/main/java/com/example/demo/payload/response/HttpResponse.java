package com.example.demo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {

    private int statusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    public HttpResponse(int statusCode,String reason, String message) {
        this.statusCode = statusCode;
        this.reason = reason;
        this.message = message;
    }
}
