package io.com.bank.dummy;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
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
}
