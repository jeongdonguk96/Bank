package io.com.bank.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TransactionEnum {
    WITHDRAW("출금"), DEPOSIT("입금"), TRANSFET("이체"), ALL("입출금내역");

    private String value;
}