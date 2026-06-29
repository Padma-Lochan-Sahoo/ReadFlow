package com.library.ReadFlow.services;


import com.library.ReadFlow.entites.User;
import com.library.ReadFlow.payload.dtos.UserDTO;

import java.util.List;

public interface UserService {

    public User getCurrentUser();

    public List<UserDTO> getAllUsers();

}
