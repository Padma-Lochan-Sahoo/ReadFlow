package com.library.ReadFlow.services;

import com.library.ReadFlow.payload.dtos.UserDTO;
import com.library.ReadFlow.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String userName,String password);
    AuthResponse signup(UserDTO req);

    void createPasswordResetToken(String email);
    void resetPassword(String token,String newPassword) throws Exception;
}
