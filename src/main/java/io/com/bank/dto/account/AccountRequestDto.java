package io.com.bank.dto.account;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

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

    // 요청으로 받을 입금 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepositRequestDto {

        @NotNull
        @Digits(integer = 8, fraction = 0)
        private Long number;

        @NotNull
        @Min(100)
        private Long amount;

        @NotEmpty
        @Pattern(regexp = "^(DEPOSIT)$")
        private String gubun;

        @NotEmpty
        @Pattern(regexp = "^[0-9]{11}$")
        private String tel;
    }

    // 요청으로 받을 출금 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WithdrawRequestDto {

        @NotNull
        @Digits(integer = 8, fraction = 0)
        private Long number;

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Long password;

        @NotEmpty
        @Pattern(regexp = "WITHDRAW")
        private String gubun;

        @NotNull
        private Long amount;
    }

    // 요청으로 받을 계좌이체 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransferRequestDto {

        @NotNull
        @Digits(integer = 8, fraction = 0)
        private Long withdrawNumber;

        @NotNull
        @Digits(integer = 8, fraction = 0)
        private Long depositNumber;

        @NotNull
        @Digits(integer = 4, fraction = 0)
        private Long withdrawPassword;

        @NotEmpty
        @Pattern(regexp = "TRANSFER")
        private String gubun;

        @NotNull
        private Long amount;
    }
}
