package com.studentManagementSystem.config;

import com.studentManagementSystem.entity.Users;
import com.studentManagementSystem.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner loadSampleData(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if(userRepo.existsByUsername("admin")){
                return;
            }
            Users users = new Users();
            users.setUsername("admin");
            users.setPassword(passwordEncoder.encode("admin123"));
            users.setActive("true");
            userRepo.save(users);
        };
    }
}
