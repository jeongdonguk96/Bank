package io.com.bank.dto.transaction;

import io.com.bank.domain.Account;
import io.com.bank.domain.Transaction;
import io.com.bank.util.CustomDateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionResponseDto {

    // 응답으로 보낼 계좌 조회 dto 객체
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionListResponseDto {
        private List<TransactionDto> transactions = new ArrayList<>();

        public TransactionListResponseDto(Account account, List<Transaction> transactions) {
            this.transactions = transactions.stream().map((transaction)-> new TransactionDto(transaction, account.getNumber()))
                    .collect(Collectors.toList());
        }

        @Data
        public class TransactionDto {
            private Long id;
            private String gubun;
            private Long amount;
            private String sender;
            private String receiver;
            private String tel;
            private String createdAt;
            private Long  balance;

            public TransactionDto(Transaction transaction, Long accountNumber) {
                this.id = transaction.getId();
                this.gubun = String.valueOf(transaction.getGubun());
                this.amount = transaction.getAmount();
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.tel = transaction.getTel();
                this.createdAt = CustomDateUtil.localDateTimeToString(transaction.getCreatedAt());

                if (transaction.getDepositAccount() == null) {
                    this.balance = transaction.getWithdrawAccountBalance();
                }
                else if (transaction.getWithdrawAccount() == null) {
                    this.balance = transaction.getDepositAccountBalance();
                }
                else {
                    if (accountNumber.longValue() == transaction.getDepositAccount().getNumber().longValue()) {
                        this.balance = transaction.getDepositAccountBalance();
                    }
                    else {
                        this.balance = transaction.getWithdrawAccountBalance();
                    }
                }
            }
        }
    }

}
