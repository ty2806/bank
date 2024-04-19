package com.example.bank.controller;

import com.example.bank.payload.UserDto;
import com.example.bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserGraphQLController {

    @Autowired
    private UserService userService;

    @QueryMapping
    public List<UserDto> allUsers() {
        return userService.getAllUsers();
    }

    @QueryMapping
    public UserDto userById(@Argument Long id) {
        return userService.getUserById(id);
    }

    @MutationMapping
    public UserDto createUser(@Argument(name = "user") UserDto userDto) {
        return userService.createUser(userDto);
    }

    @MutationMapping
    public UserDto updateUser(@Argument Long id, @Argument(name = "user") UserDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @MutationMapping
    public String deleteUser(@Argument Long id) {
        userService.deleteUser(id);
        return "user deleted";
    }
}
