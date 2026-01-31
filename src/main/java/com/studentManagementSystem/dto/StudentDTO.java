package com.studentManagementSystem.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDTO {
 
    private Long id;

    @NotBlank(message="First name is required")
    private String firstName;

    @NotBlank(message="Last name is required")
    private String lastName;

    @NotBlank(message="Email is required")
    private String email;

    @Size(max=10,message = "Max of 10 characters allowed")
    private String phoneNumber;

    @Size(max=500,message = "Max of 500 characters allowed")
    private String address;

    private boolean active = true;


}
