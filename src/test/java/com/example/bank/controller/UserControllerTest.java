package com.example.bank.controller;

import com.example.bank.payload.UserDto;
import com.example.bank.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper = new ObjectMapper();


    @Test
    void testCreateUser() throws Exception {
        UserDto userDto = new UserDto(1L, "Jack", "jack@email.com", "111 Street");
        UserDto createdUser = new UserDto(1L, "Jack", "jack@email.com", "111 Street");
        when(userService.createUser(any(UserDto.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(createdUser)));

        verify(userService).createUser(any(UserDto.class));
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<UserDto> users = Arrays.asList(new UserDto(1L, "Jack", "jack@email.com", "111 Street"),
                                            new UserDto(2L, "Alice", "alice@email.com", "222 Street"));
        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));

        verify(userService).getAllUsers();
    }

    @Test
    void testGetUserById() throws Exception {
        Long userId = 1L;
        UserDto user = new UserDto(1L, "Jack", "jack@email.com", "111 Street");
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

        verify(userService).getUserById(userId);
    }

    @Test
    void testUpdateUser() throws Exception {
        Long userId = 1L;
        UserDto userDto = new UserDto(1L, "Jack", "jack@email.com", "111 Street");
        UserDto updatedUser = new UserDto(1L, "Jack", "jack@email.com", "111 Street");
        when(userService.updateUser(eq(userId), any(UserDto.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedUser)));

        verify(userService).updateUser(eq(userId), any(UserDto.class));
    }

    @Test
    void testDeleteUser() throws Exception {
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("user deleted"));

        verify(userService).deleteUser(userId);
    }
}