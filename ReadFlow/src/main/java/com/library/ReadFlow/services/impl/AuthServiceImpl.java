package com.library.ReadFlow.services.impl;

import com.library.ReadFlow.config.JwtProvider;
import com.library.ReadFlow.domain.UserRole;
import com.library.ReadFlow.entites.PasswordResetToken;
import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.exceptions.UserException;
import com.library.ReadFlow.mapper.UserMapper;
import com.library.ReadFlow.payload.dtos.UserDTO;
import com.library.ReadFlow.payload.response.AuthResponse;
import com.library.ReadFlow.repositories.PasswordResetTokenRepository;
import com.library.ReadFlow.repositories.UserRepository;
import com.library.ReadFlow.services.AuthService;
import com.library.ReadFlow.services.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImplementation customUserServiceImplementation;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Override
    public AuthResponse login(String userName, String password) {
        Authentication authentication = authenticate(userName,password);

        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        String role = authorities.iterator().next().getAuthority();
        String token = jwtProvider.generateToken(authentication);

        User user = userRepository.findByEmail(userName);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        AuthResponse response = new AuthResponse();
        response.setTitle("Login Success");
        response.setMessage("Welcome Back "+userName);
        response.setJwt(token);
        response.setUser(UserMapper.toDTO(user));

        return response;
    }

    private Authentication authenticate(String userName, String password) {
        UserDetails userDetails = customUserServiceImplementation.loadUserByUsername(userName);

        if(userDetails == null){
            throw new UserException("User not Found With Email "+userName);
        }

        if(!passwordEncoder.matches(password,userDetails.getPassword())){
            throw new UserException("Password Not Match");
        }
        return new UsernamePasswordAuthenticationToken(userName,null,userDetails.getAuthorities());
    }

    @Override
    public AuthResponse signup(UserDTO req) {
        User user = userRepository.findByEmail(req.getEmail());
        if(user != null){
            throw new UserException("Email id already registered");
        }

        User createdUser = new User();
        createdUser.setEmail(req.getEmail());
        createdUser.setPassword(passwordEncoder.encode(req.getPassword()));
        createdUser.setPhone(req.getPhone());
        createdUser.setFullName(req.getFullName());
        createdUser.setLastLogin(LocalDateTime.now());
        createdUser.setRole(UserRole.ROLE_USER);

        User savedUser = userRepository.save(createdUser);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(),savedUser.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);

        String jwt = jwtProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt);
        response.setTitle("Welcome "+createdUser.getFullName());
        response.setMessage("Register Success");
        response.setUser(UserMapper.toDTO(savedUser));
        return response;
    }

    @Transactional
    public void createPasswordResetToken(String email) {

        String frontendUrl = "http://localhost:5173";
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("User not found with given email");
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        passwordResetTokenRepository.save(resetToken);
        String resetLink = frontendUrl+token;
        String subject="Password Reset Request";
        String body = "You requested to reset your password. Use this link (Valid 5 minute): "+resetLink;


        emailService.sendEmail(user.getEmail(), subject,body);

    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws Exception {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(
                        () -> new Exception("Token not valid!")
                );
        if(resetToken.isExpired()){
            passwordResetTokenRepository.delete(resetToken);
            throw new Exception("token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }
}
