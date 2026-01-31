package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.dto.UserDTO;
import com.studentManagementSystem.entity.Users;
import com.studentManagementSystem.repo.UserRepo;
import com.studentManagementSystem.service.UserService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserDetailsService, UserService {

    private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

    public UserServiceIMPL(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepo.findByUsername(username)
                .orElseThrow(() ->  new UsernameNotFoundException("Invalid username"));
        return User.withUsername(username)
                .password(users.getPassword())
                .disabled(!Boolean.parseBoolean(users.getActive()))
                .build();
    }

    @Override
    public Users getUserByUsername(String username) {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public UserDTO getUserDTOByUsername(String username) {
        Users user = getUserByUsername(username);
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setActive(user.getActive());
        return dto;
    }

    @Override
    public void updatePassword(String username, String newPassword) {
        Users user = getUserByUsername(username);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepo.save(user);
    }

    @Override
    public void updateUser(UserDTO userDTO) {
        Users user = getUserByUsername(userDTO.getUsername());
        if (userDTO.getNewPassword() != null && !userDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
        }
        userRepo.save(user);
    }
}
