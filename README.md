# livelihood-coupon

카드 승인 요청이 은행 계정계의 출금으로 이어지는 흐름을 두 서버로 나눠 만든 실습입니다.
같은 계좌에 출금이 동시에 몰릴 때 잔액이 어떻게 되는지 확인하려고 시작했습니다.

## 구성

| 모듈 | 역할 | 상태 |
|---|---|---|
| bank-account | 은행 계정계 서버. 계좌 조회와 출금 API | 구현 |
| card-approval | 카드 승인 서버. 승인 요청을 계정계 출금으로 넘김 | 초기 세팅만 |

## 출금 API에서 신경 쓴 것

### 동시 출금에도 잔액이 틀리지 않게

계좌 엔티티에 `@Version` 필드를 두어 낙관적 락을 적용했습니다.
두 요청이 같은 계좌를 동시에 수정하면 나중에 저장하는 쪽이 실패해, 잔액이 덮어써지지 않습니다.

출금 요청에 `simulateDelay` 값을 넣으면 조회와 저장 사이에 일부러 시간을 끌 수 있습니다.
이 값으로 경합 상황을 직접 만들어 확인할 수 있습니다. [확인 필요: 락 유무 비교 결과를 남겼는지]

### 처리 중인 금액을 따로 두기

잔액 `balance`와 별도로 처리 중인 금액 `pending`을 두고, 출금 가능 금액은 `balance - pending`으로 계산합니다.
카드 승인과 실제 출금이 나뉘었을 때 같은 돈이 두 번 쓰이지 않게 하려고 둔 필드입니다. 지금은 한 트랜잭션 안에서 더하고 빼는 단계까지만 구현했습니다.

### 요청마다 추적 번호 남기기

`TraceIdFilter`가 요청마다 UUID를 만들어 로그의 MDC와 응답 헤더 `X-Trace-Id`에 넣습니다.
같은 요청의 로그를 한 번에 찾을 수 있습니다. WORKET 프로젝트에서는 같은 방식을 넓혀 채널계와 계정계 두 서버의 로그를 하나의 TraceId로 이었습니다.

## 주요 코드

- 출금 로직: `bank-account/src/main/java/me/yeongeun/bank/service/AccountService.java`
- 계좌 엔티티와 낙관적 락: `bank-account/src/main/java/me/yeongeun/bank/model/Account.java`
- 추적 번호 필터: `bank-account/src/main/java/me/yeongeun/bank/config/TraceIdFilter.java`
- 스키마와 테스트 계좌: `bank-account/src/main/resources/schema.sql`, `data.sql`

## 실행

```bash
cd bank-account
./gradlew bootRun
```

```bash
curl -X POST localhost:8080/api/account/withdraw \
  -H "Content-Type: application/json" \
  -d '{"accountNumber":"ACC10001","amount":1000,"simulateDelay":500}'
```

> [확인 필요: 포트 번호. application.yml이 gitignore라 저장소에 없음]

## 기술

Java, Spring Boot, Spring Data JPA, MySQL
