package com.studentManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CourseDTO {

    private Long id;

    @NotBlank(message="Course name is required")
    @Size(max=150,message = "Max of 150 characters allowed")
    private String courseName;

    @NotBlank(message="Course code is required")
    private String courseCode;

    @NotBlank(message="Course duration is required")
    private String duration;

    private boolean active=true;

    @NotNull(message="Course fee is required")
    private BigDecimal fee;

    @Size(max=500,message = "Max of 500 characters allowed")
    private String description;

    private LocalDateTime createdAt;
}
