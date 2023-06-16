package io.com.bank.dummy;

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
}
