package com.example.webapi.service;

import com.example.webapi.entity.VerificationCode;
import com.example.webapi.repository.VerificationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final VerificationCodeRepository verificationCodeRepository;
    private static final int MIN_REQUEST_INTERVAL_SECONDS = 60; // 1분

    @Transactional
    public String generateVerificationCode(String phoneNumber) {
        // 이전 요청 확인
        // LocalDateTime now = LocalDateTime.now();
        // LocalDateTime minRequestTime = now.minusSeconds(MIN_REQUEST_INTERVAL_SECONDS);
        
        // boolean hasRecentRequest = verificationCodeRepository.findLatestValidCode(phoneNumber, minRequestTime)
        //         .isPresent();
        
        // if (hasRecentRequest) {
        //     throw new IllegalStateException("1분 이내에 재요청할 수 없습니다.");
        // }

        // 4자리 랜덤 숫자 생성
        String code;
        do {
            code = String.format("%04d", new Random().nextInt(10000));
        } while (verificationCodeRepository.findByVerificationCode(code).isPresent());
        
        VerificationCode verificationCode = new VerificationCode();
        verificationCode.setPhoneNumber(phoneNumber);
        verificationCode.setVerificationCode(code);
        
        verificationCodeRepository.save(verificationCode);
        
        return code;
    }

    @Transactional
    public boolean verifyCode(String phoneNumber, String code) {
        return verificationCodeRepository.findLatestValidCode(phoneNumber, LocalDateTime.now())
                .map(verificationCode -> {
                    if (verificationCode.getVerificationCode().equals(code)) {
                        verificationCode.setVerified(true);
                        verificationCode.setVerifiedAt(LocalDateTime.now());
                        verificationCodeRepository.save(verificationCode);
                        return true;
                    }
                    return false;
                })
                .orElse(false);
    }
} 