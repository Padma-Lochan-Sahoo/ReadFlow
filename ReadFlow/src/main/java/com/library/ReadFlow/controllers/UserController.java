package com.library.ReadFlow.controllers;

import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.payload.dtos.UserDTO;
import com.library.ReadFlow.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/list")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        List<UserDTO> dtos = userService.getAllUsers();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfile(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }
}
