package com.example.webapi.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "attendance_id")
    private Long attendanceId;

    @Column(name = "lecture_name", nullable = false, length = 100)
    private String lectureName;

    @Column(name = "classroom", nullable = false, length = 50)
    private String classroom;

    @Column(name = "class_time", nullable = false, length = 50)
    private String classTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "status", length = 10)
    private String status;

    @Column(name = "check_time")
    private LocalDateTime checkTime;

    @Column(name = "student_name", length = 100)
    private String studentName;
} 