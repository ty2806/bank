package com.example.bank.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Setter @Getter
@AllArgsConstructor
public class ErrorDetails {
    private HttpStatus status;
    private Date timeStamp;
    private String message;
    private String details;

}
