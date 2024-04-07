package com.example.bank.service.impl;

import com.example.bank.dao.AccountRepository;
import com.example.bank.entity.Account;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.payload.AccountDto;
import com.example.bank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        return mapToDto(accountRepository.save(mapToEntity(accountDto)));
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        return accountRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        return mapToDto(account);
    }

    @Override
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        account.setUserName(accountDto.getUserName());
        account.setAddress(accountDto.getAddress());
        account.setBalance(accountDto.getBalance());
        return mapToDto(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        accountRepository.delete(account);
    }

    private AccountDto mapToDto(Account account) {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(account.getId());
        accountDto.setUserName(account.getUserName());
        accountDto.setAddress(account.getAddress());
        accountDto.setBalance(account.getBalance());
        return accountDto;
    }

    private Account mapToEntity(AccountDto accountDto) {
        Account account = new Account();
        account.setUserName(accountDto.getUserName());
        account.setAddress(accountDto.getAddress());
        account.setBalance(accountDto.getBalance());
        return account;
    }
}
