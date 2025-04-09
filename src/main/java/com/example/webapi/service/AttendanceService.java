package com.example.webapi.service;

import com.example.webapi.entity.Attendance;
import com.example.webapi.entity.TempAttendance;
import com.example.webapi.repository.AttendanceRepository;
import com.example.webapi.repository.TempAttendanceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final TempAttendanceRepository tempAttendanceRepository;
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

        log.info("uuidddd: {}", attendance.getUuid());

        // Send message to Kafka
        try {
            String message = objectMapper.writeValueAsString(attendance);
            log.debug("Sending message to Kafka: {}", message);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
            
            // Wait for the send operation to complete
            SendResult<String, String> result = future.get();
            log.info("Successfully sent message to Kafka. Topic: {}, Partition: {}, Offset: {}", 
                    result.getRecordMetadata().topic(),
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset());
            
            // If we get here, the message was sent successfully
            TempAttendance tempAttendance = new TempAttendance();
            tempAttendance.setLectureName(attendance.getLectureName());
            tempAttendance.setClassroom(attendance.getClassroom());
            tempAttendance.setClassTime(attendance.getClassTime());
            tempAttendance.setDate(attendance.getDate());
            tempAttendance.setStatus(attendance.getStatus());
            tempAttendance.setCheckTime(attendance.getCheckTime());
            tempAttendance.setStudentName(attendance.getStudentName());
            tempAttendance.setTestSeq(attendance.getTestSeq());
            tempAttendance.setUuid(attendance.getUuid());
            
            tempAttendanceRepository.save(tempAttendance);
            log.info("Saved attendance to temp_attendance table. UUID: {}", attendance.getUuid());
            
        } catch (Exception e) {
            log.error("Failed to process attendance. UUID: {}, Error: {}", attendance.getUuid(), e.getMessage(), e);
            throw new RuntimeException("Failed to send attendance data to Kafka", e);
        }
        
        return attendance;
    }

    @Transactional(readOnly = true)
    public Long getAttendanceCount(String lectureName, String classTime) {
        return attendanceRepository.countAttendanceByLectureAndTime(lectureName, classTime);
    }
} 