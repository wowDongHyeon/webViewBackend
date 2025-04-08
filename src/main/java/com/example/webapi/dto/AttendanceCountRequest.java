package com.example.webapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AttendanceCountRequest {
    private String lectureName;
    private String classTime;
} 