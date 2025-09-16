package me.yeongeun.bank.controller;

import lombok.RequiredArgsConstructor;
import me.yeongeun.bank.dto.WithdrawRequest;
import me.yeongeun.bank.dto.WithdrawResponse;
import me.yeongeun.bank.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/withdraw")
    public ResponseEntity<WithdrawResponse> withdraw(@RequestBody WithdrawRequest request) {
        WithdrawResponse response = accountService.withdraw(request);
        return ResponseEntity.status(response.isSuccess() ? 200 : 400)
                .body(response);
    }
}