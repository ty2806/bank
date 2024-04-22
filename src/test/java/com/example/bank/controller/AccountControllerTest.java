package com.example.bank.controller;

import com.example.bank.payload.AccountDto;
import com.example.bank.service.AccountService;
import com.example.bank.service.TransactionService;
import com.example.bank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateAccount() throws Exception {
        AccountDto accountDto = new AccountDto(1L, 10001, 1000L);
        AccountDto createdAccount = new AccountDto(1L, 10001, 1000L);
        Long userId = 1L;
        when(accountService.createAccount(eq(userId), any(AccountDto.class))).thenReturn(createdAccount);

        mockMvc.perform(post("/api/v1/users/{userId}/accounts", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdAccount)));

        verify(accountService).createAccount(eq(userId), any(AccountDto.class));
    }

    @Test
    void testGetAccountsByUserId() throws Exception {
        Long userId = 1L;
        List<AccountDto> accounts = Arrays.asList(new AccountDto(1L, 10001, 1000L), new AccountDto(2L, 10002, 200L));
        when(accountService.getAccountsByUserId(userId)).thenReturn(accounts);

        mockMvc.perform(get("/api/v1/users/{userId}/accounts", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(accounts)));

        verify(accountService).getAccountsByUserId(userId);
    }

    @Test
    void testGetAccountById() throws Exception {
        Long accountId = 1L;
        AccountDto account = new AccountDto(1L, 10001, 1000L);
        when(accountService.getAccountById(accountId)).thenReturn(account);

        mockMvc.perform(get("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(account)));

        verify(accountService).getAccountById(accountId);
    }

    @Test
    void testUpdateAccount() throws Exception {
        Long accountId = 1L;
        AccountDto accountDto = new AccountDto(1L, 10001, 1000L);
        AccountDto updatedAccount = new AccountDto(1L, 10001, 1000L);
        when(accountService.updateAccount(eq(accountId), any(AccountDto.class))).thenReturn(updatedAccount);

        mockMvc.perform(put("/api/v1/accounts/{id}", accountId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedAccount)));

        verify(accountService).updateAccount(eq(accountId), any(AccountDto.class));
    }

    @Test
    void testDeleteAccount() throws Exception {
        Long accountId = 1L;
        doNothing().when(accountService).deleteAccount(accountId);

        mockMvc.perform(delete("/api/v1/accounts/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().string("account deleted."));

        verify(accountService).deleteAccount(accountId);
    }
}