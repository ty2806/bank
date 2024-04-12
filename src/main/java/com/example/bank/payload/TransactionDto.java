package com.example.bank.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    private Long id;

    @Past
    private LocalDate date;

    private String description;

    @NotNull
    private Integer reference;

    @NotNull
    private Long amount;
}
