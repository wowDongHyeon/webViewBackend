package com.example.webapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "verification_codes")
@Getter
@Setter
@NoArgsConstructor
public class VerificationCode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10-11자리 숫자여야 합니다")
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @NotBlank
    @Pattern(regexp = "^\\d{4}$", message = "인증번호는 4자리 숫자여야 합니다")
    @Column(name = "verification_code", nullable = false, length = 4)
    private String verificationCode;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expired_at", nullable = false)
    private LocalDateTime expiredAt;

    @Column(name = "is_verified", nullable = false)
    private boolean isVerified = false;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @PrePersist
    public void setExpiredAt() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        this.expiredAt = this.createdAt.plusMinutes(3); // 3분 후 만료
    }
} 