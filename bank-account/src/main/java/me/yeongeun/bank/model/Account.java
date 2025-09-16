package me.yeongeun.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA용 기본 생성자
@AllArgsConstructor // 모든 final 필드에 대한 생성자
@Builder
public class Account {

    @Id
    private String accountNumber;

    private long balance;
    private long pending;

    @Version
    private long version; // 낙관적 락

    // -----------------------
    // 정적 팩토리 메서드
    // -----------------------
    public static Account of(String accountNumber, long balance) {
        return Account.builder()
                .accountNumber(accountNumber)
                .balance(balance)
                .pending(0)
                .build();
    }

    // -----------------------
    // 비즈니스 로직
    // -----------------------
    public long getAvailable() {
        return balance - pending;
    }

    public void addPending(long amount) {
        pending += amount;
    }

    public void removePending(long amount) {
        pending -= amount;
    }

    public void deductBalance(long amount) {
        balance -= amount;
    }
}