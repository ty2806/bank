package com.example.bank.controller;

import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/{accountId}/transactions")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable(name = "accountId") Long accountId,
                                                            @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(transactionService.createTransaction(accountId, transactionDto), HttpStatus.CREATED);
    }

    @GetMapping("/{accountId}/transactions")
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

    @GetMapping("/{accountId}/transactions/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable(name = "accountId") Long accountId,
                                                             @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(accountId, id));
    }

    @PutMapping("/{accountId}/transactions/{id}")
    public ResponseEntity<TransactionDto> updateTransaction(@PathVariable(name = "accountId") Long accountId,
                                            @PathVariable(name = "accountId") Long id,
                                            @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.updateTransaction(accountId, id, transactionDto));
    }

    @DeleteMapping("/{accountId}/transactions/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable(name = "accountId") Long accountId,
                                                    @PathVariable(name = "id") Long id) {
        transactionService.deleteTransaction(accountId, id);
        return ResponseEntity.ok("transaction deleted");
    }
}
