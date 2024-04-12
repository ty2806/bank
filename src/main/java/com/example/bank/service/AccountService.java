package com.example.bank.service;

import com.example.bank.payload.AccountDto;

import java.util.List;

public interface AccountService {
    public AccountDto createAccount(Long userId, AccountDto accountDto);

    public List<AccountDto> getAccountsByUserId(Long userId);

    public AccountDto getAccountById(Long id);

    public AccountDto updateAccount(Long id, AccountDto accountDto);

    public void deleteAccount(Long id);
}
