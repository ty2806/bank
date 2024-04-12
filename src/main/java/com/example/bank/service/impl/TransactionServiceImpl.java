package com.example.bank.service.impl;

import com.example.bank.dao.AccountRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.exception.BankAPIException;
import com.example.bank.exception.QueryFormatInvalidException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionDto createTransaction(Long accountId, TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("accounts", "id", accountId));

        transaction.setAccount(account);
        account.setBalance(account.getBalance()+transaction.getAmount());
        accountRepository.save(account);
        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        if (!accountRepository.existsById(accountId)) throw new ResourceNotFoundException("accounts", "id", accountId);
        return transactionRepository
                .findByAccountId(accountId)
                .stream()
                .map(a->modelMapper.map(a, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountIdBetweenDate(Long accountId, LocalDate startDate, LocalDate endDate) {
        if (!accountRepository.existsById(accountId)) throw new ResourceNotFoundException("accounts", "id", accountId);
        if (startDate.isAfter(endDate)) throw new QueryFormatInvalidException("query start date is after end date");
        return transactionRepository
                .findByAccountIdAndDateBetween(accountId, startDate, endDate).stream()
                .map(a->modelMapper.map(a, TransactionDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getTransactionById(Long id) {
        return modelMapper.map(transactionRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("transactions", "id", id)),
                TransactionDto.class);
    }

    @Override
    @Transactional
    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto) {
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("transactions", "id", id));

        Long diff = transactionDto.getAmount() - transaction.getAmount();
        Account account = transaction.getAccount();
        account.setBalance(account.getBalance()+diff);
        accountRepository.save(account);

        transaction.setDate(transactionDto.getDate());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setReference(transactionDto.getReference());
        transaction.setAmount(transactionDto.getAmount());
        return modelMapper.map(transactionRepository.save(transaction), TransactionDto.class);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("transactions", "id", id));

        Account account = transaction.getAccount();
        account.setBalance(account.getBalance()-transaction.getAmount());
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }
}
