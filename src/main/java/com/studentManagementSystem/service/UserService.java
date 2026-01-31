package com.studentManagementSystem.service;

import com.studentManagementSystem.dto.UserDTO;
import com.studentManagementSystem.entity.Users;

public interface UserService {
    Users getUserByUsername(String username);
    UserDTO getUserDTOByUsername(String username);
    void updatePassword(String username, String newPassword);
    void updateUser(UserDTO userDTO);
}
