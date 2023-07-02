package io.com.bank.dummy;

import io.com.bank.domain.*;
import io.com.bank.repository.AccountRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    protected Member newMember(String username, String fullname) {
        String encodedPassword = bCryptPasswordEncoder.encode("1234");

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .email(username + "@gmail.com")
                .fullname(fullname)
                .role(RoleEnum.CUSTOMER)
                .build();
    }

    protected Member newMember2(String username, String fullname) {
        String encodedPassword = bCryptPasswordEncoder.encode("1234");

        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .email(username + "@gmail.com")
                .fullname(fullname)
                .role(RoleEnum.ADMIN)
                .build();
    }

    protected Member newMockMember(Long id, String username, String fullname) {
        String encodedPassword = bCryptPasswordEncoder.encode("1234");

        return Member.builder()
                .id(id)
                .username(username)
                .password(encodedPassword)
                .email(username + "@gmail.com")
                .fullname(fullname)
                .role(RoleEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Account newAccount(Long number, Member member) {
        return Account.builder()
                .number(number)
                .password(1234L)
                .balance(1000L)
                .member(member)
                .build();
    }

    protected Account newMockAccount(Long id, Long number, Long balance, Member member) {
        return Account.builder()
                .id(id)
                .number(number)
                .password(1234L)
                .balance(balance)
                .member(member)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newMockDepositTransaction(Long id, Account findAccount) {
        return Transaction.builder()
                .id(id)
                .depositAccount(findAccount)
                .withdrawAccount(null)
                .depositAccountBalance(findAccount.getBalance())
                .withdrawAccountBalance(null)
                .amount(1000L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(findAccount.getNumber().toString())
                .tel("01012345678")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
        account.withdraw(100L);

        if (accountRepository != null) {
            accountRepository.save(account);
        }

        return Transaction.builder()
                .withdrawAccount(account)
                .depositAccount(null)
                .withdrawAccountBalance(account.getBalance())
                .depositAccountBalance(null)
                .amount(100L)
                .gubun(TransactionEnum.WITHDRAW)
                .sender(String.valueOf(account.getMember().getFullname()))
                .receiver(null)
                .build();
    }

    protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
        account.deposit(100L);

        if (accountRepository != null) {
            accountRepository.save(account);
        }

        return Transaction.builder()
                .withdrawAccount(null)
                .depositAccount(account)
                .withdrawAccountBalance(null)
                .depositAccountBalance(account.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(String.valueOf(account.getMember().getFullname()))
                .tel("01012345678")
                .build();
    }

    protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount, AccountRepository accountRepository) {
        withdrawAccount.withdraw(100L);
        depositAccount.deposit(100L);

        if (accountRepository != null) {
            accountRepository.save(withdrawAccount);
            accountRepository.save(depositAccount);
        }

        return Transaction.builder()
                .withdrawAccount(withdrawAccount)
                .depositAccount(depositAccount)
                .withdrawAccountBalance(withdrawAccount.getBalance())
                .depositAccountBalance(depositAccount.getBalance())
                .amount(100L)
                .gubun(TransactionEnum.TRANSFER)
                .sender(String.valueOf(withdrawAccount.getMember().getFullname()))
                .receiver(String.valueOf(depositAccount.getMember().getFullname()))
                .build();
    }
}
