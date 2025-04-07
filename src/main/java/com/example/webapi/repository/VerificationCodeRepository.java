package com.example.webapi.repository;

import com.example.webapi.entity.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
    
    @Query("SELECT v FROM VerificationCode v WHERE v.phoneNumber = :phoneNumber " +
           "AND v.isVerified = false AND v.expiredAt > :now " +
           "ORDER BY v.createdAt DESC")
    Optional<VerificationCode> findLatestValidCode(
            @Param("phoneNumber") String phoneNumber,
            @Param("now") LocalDateTime now);

    Optional<VerificationCode> findByVerificationCode(String code);
} 