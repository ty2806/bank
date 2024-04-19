package com.example.bank.controller;

import com.example.bank.payload.AccountDto;
import com.example.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AccountGraphQLController {

    @Autowired
    private AccountService accountService;

    @QueryMapping
    public List<AccountDto> accountsByUserId(@Argument Long userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @QueryMapping
    public AccountDto accountById(@Argument Long id) {
        return accountService.getAccountById(id);
    }

    @MutationMapping
    public AccountDto createAccount(@Argument Long userId, @Argument(name = "account") AccountDto accountDto) {
        return accountService.createAccount(userId, accountDto);
    }

    @MutationMapping
    public AccountDto updateAccount(@Argument Long id, @Argument(name = "account") AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto);
    }

    @MutationMapping
    public String deleteAccount(@Argument Long id) {
        accountService.deleteAccount(id);
        return "Account deleted";
    }
}
