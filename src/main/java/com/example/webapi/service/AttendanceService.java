package com.example.webapi.service;

import com.example.webapi.entity.Attendance;
import com.example.webapi.repository.AttendanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    @Transactional
    public Attendance checkAttendance(Attendance attendance) {
        // Set check time to current time
        attendance.setCheckTime(LocalDateTime.now());
        
        // If status is not set, default to "미정"
        if (attendance.getStatus() == null) {
            attendance.setStatus("미정");
        }
        
        // Attendance savedAttendance = attendanceRepository.save(attendance);
        
        // Send message to Kafka
        try {
            String message = objectMapper.writeValueAsString(attendance);
            kafkaTemplate.send(topic, message);
        } catch (Exception e) {
            // Log error but don't throw it to prevent transaction rollback
            e.printStackTrace();
        }
        
        return attendance;
    }

    @Transactional(readOnly = true)
    public Long getAttendanceCount(String lectureName, String classTime) {
        return attendanceRepository.countAttendanceByLectureAndTime(lectureName, classTime);
    }
} 