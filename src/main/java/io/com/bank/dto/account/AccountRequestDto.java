package io.com.bank.dto.account;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

public class AccountRequestDto {

    // 요청으로 받을 계좌 생성 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequestDto {

        @NotNull
        @Digits(integer = 8, fraction = 0)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Long password;

        public Account toEntity(Member member) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .member(member)
                    .build();
        }
    }
}
