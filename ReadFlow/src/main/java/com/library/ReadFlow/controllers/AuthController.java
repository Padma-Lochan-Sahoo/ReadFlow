package com.library.ReadFlow.controllers;

import com.library.ReadFlow.payload.dtos.UserDTO;
import com.library.ReadFlow.payload.request.ForgotPasswordRequest;
import com.library.ReadFlow.payload.request.LoginRequest;
import com.library.ReadFlow.payload.request.ResetPasswordRequest;
import com.library.ReadFlow.payload.response.ApiResponse;
import com.library.ReadFlow.payload.response.AuthResponse;
import com.library.ReadFlow.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signupHandler(@Valid @RequestBody UserDTO req){
        AuthResponse res = authService.signup(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@Valid @RequestBody LoginRequest req){
        AuthResponse res = authService.login(req.getEmail(), req.getPassword());
        return ResponseEntity.ok(res);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(
            @RequestBody ForgotPasswordRequest req){
        authService.createPasswordResetToken(req.getEmail());
        ApiResponse res = new ApiResponse(
                "A Reset link was sent to your email",
                true
        );
        return ResponseEntity.ok(res);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(
            @RequestBody ResetPasswordRequest req) throws Exception {
        authService.resetPassword(req.getToken(),req.getPassword());
        ApiResponse res = new ApiResponse(
                "Password reset successful",
                true
        );
        return ResponseEntity.ok(res);
    }
}
