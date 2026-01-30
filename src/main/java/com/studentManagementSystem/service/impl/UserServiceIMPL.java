package com.studentManagementSystem.service.impl;

import com.studentManagementSystem.entity.Users;
import com.studentManagementSystem.repo.UserRepo;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceIMPL implements UserDetailsService {


    private UserRepo userRepo;

    public UserServiceIMPL(UserRepo userRepo) {
        this.userRepo = userRepo;
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
}
