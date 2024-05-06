package com.example.demo.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HttpResponse {
    private int statusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;
}
