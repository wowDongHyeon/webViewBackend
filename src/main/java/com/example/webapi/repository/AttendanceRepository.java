package com.example.webapi.repository;

import com.example.webapi.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    
    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.lectureName = :lectureName AND a.classTime = :classTime AND a.status = '출석'")
    Long countAttendanceByLectureAndTime(@Param("lectureName") String lectureName, @Param("classTime") String classTime);
} 