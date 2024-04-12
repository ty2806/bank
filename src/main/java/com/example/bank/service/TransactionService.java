package com.example.bank.service;

import com.example.bank.payload.TransactionDto;

import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    public TransactionDto createTransaction(Long accountId, TransactionDto transactionDto);

    public List<TransactionDto> getTransactionsByAccountId(Long accountId);

    public List<TransactionDto> getTransactionsByAccountIdBetweenDate(Long accountId, LocalDate startDate, LocalDate endDate);

    public TransactionDto getTransactionById(Long id);

    public TransactionDto updateTransaction(Long id, TransactionDto transactionDto);

    public void deleteTransaction(Long id);
}
