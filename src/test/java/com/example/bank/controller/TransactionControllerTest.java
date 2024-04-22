package com.example.bank.controller;

import com.example.bank.payload.TransactionDto;
import com.example.bank.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void createTransaction() throws Exception {
        Long accountId = 1L;
        TransactionDto transactionDto = new TransactionDto(1L, LocalDate.of(2024, 1, 10), "pay bill", 1234, -1000L);
        TransactionDto createdTransaction = new TransactionDto(1L, LocalDate.of(2024, 1, 10), "pay bill", 1234, -1000L);
        when(transactionService.createTransaction(eq(accountId), any(TransactionDto.class))).thenReturn(createdTransaction);

        mockMvc.perform(post("/api/v1/accounts/{accountId}/transactions", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdTransaction)));

        verify(transactionService).createTransaction(eq(accountId), any(TransactionDto.class));
    }

    @Test
    void getTransactionsByAccountId() throws Exception {
        Long accountId = 1L;
        List<TransactionDto> transactions = Arrays.asList(new TransactionDto(1L, LocalDate.of(2024, 1, 10), "pay bill", 1234, -1000L),
                new TransactionDto(2L, LocalDate.of(2024, 3, 18), "receive payroll", 4561, 1000L));
        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/api/v1/accounts/{accountId}/transactions", accountId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));

        verify(transactionService).getTransactionsByAccountId(accountId);
    }

    @Test
    void getTransactionsByAccountIdWithDates() throws Exception {
        Long accountId = 1L;
        LocalDate startDate = LocalDate.of(2022, 1, 1);
        LocalDate endDate = LocalDate.of(2022, 1, 31);
        List<TransactionDto> transactions = Arrays.asList(new TransactionDto(1L, LocalDate.of(2024, 1, 10), "pay bill", 1234, -1000L),
                new TransactionDto(2L, LocalDate.of(2024, 1, 18), "receive payroll", 4561, 1000L));
        when(transactionService.getTransactionsByAccountIdBetweenDate(eq(accountId), eq(startDate), eq(endDate)))
                .thenReturn(transactions);

        mockMvc.perform(get("/api/v1/accounts/{accountId}/transactions", accountId)
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transactions)));

        verify(transactionService).getTransactionsByAccountIdBetweenDate(eq(accountId), eq(startDate), eq(endDate));
    }

    @Test
    void getTransactionById() throws Exception {
        Long transactionId = 1L;
        TransactionDto transaction = new TransactionDto(2L, LocalDate.of(2024, 3, 18), "receive payroll", 4561, 1000L);
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        mockMvc.perform(get("/api/v1/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaction)));

        verify(transactionService).getTransactionById(transactionId);
    }

    @Test
    void updateTransaction() throws Exception {
        Long transactionId = 1L;
        TransactionDto transactionDto = new TransactionDto(2L, LocalDate.of(2024, 3, 18), "receive payroll", 4561, 1000L);
        TransactionDto updatedTransaction = new TransactionDto(2L, LocalDate.of(2024, 3, 18), "receive payroll", 4561, 1000L);
        when(transactionService.updateTransaction(eq(transactionId), any(TransactionDto.class))).thenReturn(updatedTransaction);

        mockMvc.perform(put("/api/v1/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedTransaction)));

        verify(transactionService).updateTransaction(eq(transactionId), any(TransactionDto.class));
    }

    @Test
    void deleteTransaction() throws Exception {
        Long transactionId = 1L;
        doNothing().when(transactionService).deleteTransaction(transactionId);

        mockMvc.perform(delete("/api/v1/transactions/{id}", transactionId))
                .andExpect(status().isOk())
                .andExpect(content().string("transaction deleted"));

        verify(transactionService).deleteTransaction(transactionId);
    }
}