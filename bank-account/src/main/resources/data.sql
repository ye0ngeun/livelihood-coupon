USE account_db;

-- 테스트용 계좌 데이터
INSERT INTO account(account_number, balance, pending, version)
VALUES
    ('ACC10001', 100000, 0, 0),
    ('ACC10002', 50000, 0, 0),
    ('ACC10003', 0, 0, 0),
    ('ACC10004', 200000, 0, 0);

-- 추후 스크립트로 데이터 생성 후 삽입 ( 1000개 )