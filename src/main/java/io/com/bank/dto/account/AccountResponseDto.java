package io.com.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.util.CustomDateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    // 응답으로 보낼 계좌 생성 dto 객체
    @Data
    @NoArgsConstructor
    public static class AccountListResponseDto {
        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListResponseDto(Member member, List<Account> accounts) {
            this.fullname = member.getFullname();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }

        @Data
        @NoArgsConstructor
        public class AccountDto {
            private Long id;
            private Long number;
            private Long balance;

            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }

    // 응답으로 보낼 입금 dto 객체
    @Data
    @NoArgsConstructor
    public static class DepositResponseDto {
        private Long id;
        private Long number;
        private TransactionDto transactionDto;

        public DepositResponseDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Data
        @NoArgsConstructor
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String tel;
            private String createdAt;

            @JsonIgnore
            private Long depositAccountBalance;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = String.valueOf(transaction.getGubun());
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.depositAccountBalance = transaction.getDepositAccountBalance();
                this.tel = transaction.getTel();
                this.createdAt = String.valueOf(transaction.getCreatedAt());
            }
        }
    }

    // 응답으로 보낼 출금 dto 객체
    @Data
    @NoArgsConstructor
    public static class WithdrawResponseDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transactionDto;

        public WithdrawResponseDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Data
        @NoArgsConstructor
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = String.valueOf(transaction.getGubun());
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = String.valueOf(transaction.getCreatedAt());
            }
        }
    }

    // 응답으로 보낼 계좌이체 dto 객체
    @Data
    @NoArgsConstructor
    public static class TransferResponseDto {
        private Long id;
        private Long number;
        private Long balance;
        private TransactionDto transactionDto;

        public TransferResponseDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactionDto = new TransactionDto(transaction);
        }

        @Data
        @NoArgsConstructor
        public class TransactionDto {
            private Long id;
            private String gubun;
            private String sender;
            private String receiver;
            private Long amount;
            private String createdAt;

//            @JsonIgnore
            private Long depositAccountBalance;

            public TransactionDto(Transaction transaction) {
                this.id = transaction.getId();
                this.gubun = String.valueOf(transaction.getGubun());
                this.sender = transaction.getSender();
                this.receiver = transaction.getReceiver();
                this.amount = transaction.getAmount();
                this.createdAt = String.valueOf(transaction.getCreatedAt());
                this.depositAccountBalance = transaction.getDepositAccountBalance();
            }
        }
    }

    // 응답으로 보낼 계좌 상세 dto 객체
    @Data
    @NoArgsConstructor
    public static class AccountDetailResponseDto {
        private Long id;
        private Long number;
        private Long balance;
        private List<TransactionDto> transactions = new ArrayList<>();

        public AccountDetailResponseDto(Account account, List<Transaction> transactions) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transactions = transactions.stream()
                    .map((transaction) -> new TransactionDto(transaction, account.getNumber()))
                    .collect(Collectors.toList());
        }

        @Data
        @NoArgsConstructor
        public class TransactionDto {
            private Long id;
            private String gubun;
            private Long amount;
            private String sender;
            private String receiver;
            private String tel;
            private String createdAt;
            private Long balance;


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
