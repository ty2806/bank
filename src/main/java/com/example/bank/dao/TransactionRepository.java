package com.example.bank.dao;

import com.example.bank.entity.Account;
import com.example.bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId);
    List<Transaction> findByAccountIdAndDateBetween(Long accountId, LocalDate startDate, LocalDate endDate);
}
