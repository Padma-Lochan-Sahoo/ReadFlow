package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.domain.UserRole;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        initializeAdminUser();
    }

    private void initializeAdminUser(){
        String adminEmail = "mrpadma33@gmail.com";
        String adminPassword="codewithpadma";
        if(userRepository.findByEmail(adminEmail) == null){
            User user = User.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Padma Lochan Sahoo")
                    .role(UserRole.ROLE_ADMIN)
                    .build();

            User admin = userRepository.save(user);
        }

    }
}
