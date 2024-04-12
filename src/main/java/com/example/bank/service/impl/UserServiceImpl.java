package com.example.bank.service.impl;

import com.example.bank.dao.AccountRepository;
import com.example.bank.dao.TransactionRepository;
import com.example.bank.dao.UserRepository;
import com.example.bank.entity.Account;
import com.example.bank.entity.User;
import com.example.bank.exception.ResourceNotFoundException;
import com.example.bank.payload.UserDto;
import com.example.bank.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        return modelMapper.map(userRepository.save(modelMapper.map(userDto, User.class)), UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(a->modelMapper.map(a, UserDto.class)).collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("users", "id", id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("users", "id", id));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAddress(userDto.getAddress());
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("users", "id", id));
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            transactionRepository.deleteAll(transactionRepository.findByAccountId(account.getId()));
            accountRepository.delete(account);
        }
        userRepository.delete(user);
    }
}
