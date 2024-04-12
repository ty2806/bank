package com.example.bank.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Setter @Getter
@AllArgsConstructor
public class QueryFormatInvalidException extends RuntimeException{

    private String message;
}
