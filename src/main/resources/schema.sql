CREATE TABLE IF NOT EXISTS verification_codes (
    id SERIAL PRIMARY KEY,
    phone_number VARCHAR(20) NOT NULL,
    verification_code VARCHAR(4) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expired_at TIMESTAMP NOT NULL,
    is_verified BOOLEAN NOT NULL DEFAULT FALSE,
    verified_at TIMESTAMP,
    CONSTRAINT chk_verification_code_length CHECK (LENGTH(verification_code) = 4)
);

-- 인덱스 생성 (전화번호와 생성시간 기준으로 조회가 많을 것 같습니다)
CREATE INDEX IF NOT EXISTS idx_verification_codes_phone_created 
ON verification_codes(phone_number, created_at);

-- 만료된 인증번호는 주기적으로 삭제하기 위한 인덱스
CREATE INDEX IF NOT EXISTS idx_verification_codes_expired 
ON verification_codes(expired_at); 