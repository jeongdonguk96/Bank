package io.com.bank.dto.account;

import io.com.bank.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AccountResponseDto {

    // 응답으로 보낼 계좌 생성 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateResponseDto {
        private Long id;
        private Long number;
        private Long balance;

        public CreateResponseDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
}
