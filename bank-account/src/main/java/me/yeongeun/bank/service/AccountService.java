package me.yeongeun.bank.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.yeongeun.bank.dto.WithdrawRequest;
import me.yeongeun.bank.dto.WithdrawResponse;
import me.yeongeun.bank.model.Account;
import me.yeongeun.bank.repository.AccountRepository;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;

    @Transactional
    public WithdrawResponse withdraw(WithdrawRequest request) {
        String traceId = MDC.get("traceId");
        try {
            Account account = accountRepository.findById(request.getAccountNumber())
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Optional: 지연 시뮬레이션
            if (request.getSimulateDelay() > 0) {
                Thread.sleep(request.getSimulateDelay());
            }

            if (account.getAvailable() < request.getAmount()) {
                log.warn("[{}] Insufficient funds: account={}", traceId, account.getAccountNumber());
                return WithdrawResponse.fail("Insufficient funds");
            }

            account.addPending(request.getAmount());
            account.deductBalance(request.getAmount());
            account.removePending(request.getAmount());

            accountRepository.save(account);

            log.info("[{}] Withdraw success: account={}, balance={}, pending={}",
                    traceId, account.getAccountNumber(), account.getBalance(), account.getPending());

            return WithdrawResponse.success(account.getBalance(), account.getPending());
        } catch (Exception e) {
            log.error("[{}] Withdraw error: account={}, message={}",
                    traceId, request.getAccountNumber(), e.getMessage(), e);
            return WithdrawResponse.fail(e.getMessage());
        }
    }
}