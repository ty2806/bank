package com.example.bank.controller;

import com.example.bank.payload.AccountDto;
import com.example.bank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<AccountDto> createAccount(@PathVariable(name = "userId") Long userId, @Valid @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountService.createAccount(userId, accountDto), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/accounts")
    public List<AccountDto> getAccountsByUserId(@PathVariable(name = "userId") Long userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @PutMapping("/accounts/{id}")
    public ResponseEntity<AccountDto> updateAccount(@PathVariable(name = "id") Long id,
                                                    @Valid @RequestBody AccountDto accountDto) {
        return ResponseEntity.ok(accountService.updateAccount(id, accountDto));
    }

    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable(name = "id") Long id) {
        accountService.deleteAccount(id);
        return new ResponseEntity<>("account deleted.", HttpStatus.OK);
    }
}
