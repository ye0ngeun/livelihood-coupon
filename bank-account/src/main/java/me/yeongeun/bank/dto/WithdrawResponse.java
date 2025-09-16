package me.yeongeun.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class WithdrawResponse {
    private final boolean success;
    private final long balance;
    private final long pending;
    private final String message;

    public static WithdrawResponse success(long balance, long pending) {
        return WithdrawResponse.builder()
                .success(true)
                .balance(balance)
                .pending(pending)
                .message(null)
                .build();
    }

    public static WithdrawResponse fail(String message) {
        return WithdrawResponse.builder()
                .success(false)
                .balance(0)
                .pending(0)
                .message(message)
                .build();
    }
}