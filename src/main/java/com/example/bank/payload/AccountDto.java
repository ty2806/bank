package com.example.bank.payload;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountDto {
    private Long id;

    @NotNull
    @Range(min = 10000, max = 99999)
    private Integer accountNumber;

    private Long balance;
}
