package com.studentManagementSystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "enrollment")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Enrollment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false)
    private LocalDateTime enrolledDate;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 50)
    private String status;

    @PrePersist
    public void onCreate() {
        if (this.enrolledDate == null) {
            this.enrolledDate = LocalDateTime.now();
        }
    }
}
