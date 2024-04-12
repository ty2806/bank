package com.example.bank.controller;

import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable(name = "accountId") Long accountId,
                                                            @Valid @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(transactionService.createTransaction(accountId, transactionDto), HttpStatus.CREATED);
    }

    @GetMapping("/accounts/{accountId}/transactions")
    public List<TransactionDto> getTransactionsByAccountId(
            @PathVariable(name = "accountId") Long accountId,
            @RequestParam(name = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> startDate,
            @RequestParam(name = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> endDate)
    {
        if (startDate.isPresent() && endDate.isPresent()) {
            return transactionService.getTransactionsByAccountIdBetweenDate(accountId, startDate.get(), endDate.get());
        } else {
            return transactionService.getTransactionsByAccountId(accountId);
        }
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/transactions/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "id") Long id,
                                                            @Valid @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transactionDto));
    }

    @DeleteMapping("/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable(name = "id") Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("transaction deleted");
    }
}
