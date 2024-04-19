package com.example.bank.controller;

import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;

@Controller
public class TransactionGraphQLController {

    @Autowired
    private TransactionService transactionService;

    @QueryMapping
    public List<TransactionDto> transactionsByAccountId(@Argument Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }

    @QueryMapping
    public List<TransactionDto> transactionsByAccountIdBetweenDate(@Argument Long accountId, @Argument LocalDate startDate, @Argument LocalDate endDate) {
        return transactionService.getTransactionsByAccountIdBetweenDate(accountId, startDate, endDate);
    }

    @QueryMapping
    public TransactionDto transactionById(@Argument Long id) {
        return transactionService.getTransactionById(id);
    }

    @MutationMapping
    public TransactionDto createTransaction(@Argument Long accountId, @Argument(name = "transaction") TransactionDto transactionDto) {
        return transactionService.createTransaction(accountId, transactionDto);
    }

    @MutationMapping
    public TransactionDto updateTransaction(@Argument Long id, @Argument(name = "transaction") TransactionDto transactionDto) {
        return transactionService.updateTransaction(id, transactionDto);
    }

    @MutationMapping
    public String deleteTransaction(@Argument Long id) {
        transactionService.deleteTransaction(id);
        return "transaction deleted";
    }
}
