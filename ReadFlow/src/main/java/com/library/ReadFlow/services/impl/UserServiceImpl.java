package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.exceptions.UserException;
import com.library.ReadFlow.mapper.UserMapper;
import com.library.ReadFlow.payload.dtos.UserDTO;
import com.library.ReadFlow.repositories.UserRepository;
import com.library.ReadFlow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email);
        if(user == null){
            throw new UserException("User Not Found!");
        }

        return user;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();

        return users.stream().map(
                UserMapper::toDTO
        ).collect(Collectors.toList());
    }
}
