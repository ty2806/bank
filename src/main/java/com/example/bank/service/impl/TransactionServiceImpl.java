package com.example.bank.service.impl;

import com.example.bank.dao.AccountRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import com.example.bank.exception.BlogAPIException;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public TransactionDto createTransaction(Long accountId, TransactionDto transactionDto) {
        Transaction transaction = mapToEntity(transactionDto);
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("accounts", "id", accountId));

        transaction.setAccount(account);
        account.setBalance(account.getBalance()+transaction.getAmount());
        accountRepository.save(account);
        return mapToDto(transactionRepository.save(transaction));
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountId(Long accountId) {
        return transactionRepository
                .findByAccountId(accountId)
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getTransactionsByAccountIdBetweenDate(Long accountId, LocalDate startDate, LocalDate endDate) {
        return transactionRepository
                .findByAccountIdAndDateBetween(accountId, startDate, endDate).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public TransactionDto getTransactionById(Long accountId, Long id) {
        return mapToDto(getTransaction(accountId, id));
    }

    @Override
    public TransactionDto updateTransaction(Long accountId, Long id, TransactionDto transactionDto) {
        Transaction transaction = getTransaction(accountId, id);

        Long diff = transactionDto.getAmount() - transaction.getAmount();
        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("accounts", "id", accountId));
        account.setBalance(account.getBalance()+diff);
        accountRepository.save(account);

        transaction.setDate(transactionDto.getDate());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setReference(transactionDto.getReference());
        transaction.setAmount(transactionDto.getAmount());
        return mapToDto(transactionRepository.save(transaction));
    }

    @Override
    public void deleteTransaction(Long accountId, Long id) {
        Transaction transaction = getTransaction(accountId, id);

        Account account = accountRepository.findById(accountId).orElseThrow(()->new ResourceNotFoundException("accounts", "id", accountId));
        account.setBalance(account.getBalance()-transaction.getAmount());
        accountRepository.save(account);

        transactionRepository.delete(transaction);
    }

    private TransactionDto mapToDto(Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transaction.getId());
        transactionDto.setDate(transaction.getDate());
        transactionDto.setDescription(transaction.getDescription());
        transactionDto.setReference(transaction.getReference());
        transactionDto.setAmount(transaction.getAmount());
        return transactionDto;
    }

    private Transaction mapToEntity(TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setDate(transactionDto.getDate());
        transaction.setDescription(transactionDto.getDescription());
        transaction.setReference(transactionDto.getReference());
        transaction.setAmount(transactionDto.getAmount());
        return transaction;
    }

    private Transaction getTransaction(Long accountId, Long id) {
        Account account = accountRepository
                .findById(accountId)
                .orElseThrow(()->new ResourceNotFoundException("accounts", "id", accountId));
        Transaction transaction = transactionRepository
                .findById(id)
                .orElseThrow(()->new ResourceNotFoundException("transactions", "id", id));

        if (!transaction.getAccount().getId().equals(account.getId())) throw new BlogAPIException(HttpStatus.BAD_REQUEST, "The transaction does not belong to the account");
        return transaction;
    }
}
