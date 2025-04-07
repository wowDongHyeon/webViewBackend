package com.example.webapi.controller;

import com.example.webapi.service.VerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/verification")
@RequiredArgsConstructor
public class VerificationController {

    private final VerificationService verificationService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateCode(@RequestParam String phoneNumber) {
        String code = verificationService.generateVerificationCode(phoneNumber);
        return ResponseEntity.ok(code);
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCode(
            @RequestParam String phoneNumber,
            @RequestParam String code) {
        boolean isValid = verificationService.verifyCode(phoneNumber, code);
        return ResponseEntity.ok(isValid);
    }
} 