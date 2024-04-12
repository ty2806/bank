package com.example.bank.service.impl;

import com.example.bank.dao.AccountRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.entity.User;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.payload.AccountDto;
import com.example.bank.service.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AccountDto createAccount(Long userId, AccountDto accountDto) {
        Account account = modelMapper.map(accountDto, Account.class);
        User user = userRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("users", "id", userId));

        account.setUser(user);
        return modelMapper.map(accountRepository.save(account), AccountDto.class);
    }

    @Override
    public List<AccountDto> getAccountsByUserId(Long userId) {
        if (!userRepository.existsById(userId)) throw new ResourceNotFoundException("users", "id", userId);
        return accountRepository.findByUserId(userId).stream().map(a->modelMapper.map(a, AccountDto.class)).collect(Collectors.toList());
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        return modelMapper.map(account, AccountDto.class);
    }

    @Override
    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        account.setAccountNumber(accountDto.getAccountNumber());
        account.setBalance(accountDto.getBalance());
        return modelMapper.map(accountRepository.save(account), AccountDto.class);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("accounts", "id", id));
        transactionRepository.deleteAll(transactionRepository.findByAccountId(id));
        accountRepository.delete(account);
    }
}
