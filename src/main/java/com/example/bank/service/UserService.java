package com.example.bank.service;

import com.example.bank.payload.UserDto;

import java.util.List;

public interface UserService {

    public UserDto createUser(UserDto userDto);

    public List<UserDto> getAllUsers();

    public UserDto getUserById(Long id);

    public UserDto updateUser(Long id, UserDto userDto);

    public void deleteUser(Long id);
}
