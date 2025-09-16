package me.yeongeun.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WithdrawRequest {
    private final String accountNumber;
    private final long amount;
    private final long simulateDelay; // ms, 테스트용 지연
}