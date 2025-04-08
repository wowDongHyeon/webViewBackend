package com.example.webapi.service;

import com.example.webapi.entity.Attendance;
import com.example.webapi.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public Attendance checkAttendance(Attendance attendance) {
        // Set check time to current time
        attendance.setCheckTime(LocalDateTime.now());
        
        // If status is not set, default to "미정"
        if (attendance.getStatus() == null) {
            attendance.setStatus("미정");
        }
        
        return attendanceRepository.save(attendance);
    }
} 