DROP DATABASE IF EXISTS account_db;
CREATE DATABASE account_db;
USE account_db;

-- 계정 테이블
CREATE TABLE account (
    account_number VARCHAR(20) NOT NULL PRIMARY KEY,
    balance BIGINT NOT NULL,
    pending BIGINT NOT NULL,
    version BIGINT NOT NULL DEFAULT 0
);