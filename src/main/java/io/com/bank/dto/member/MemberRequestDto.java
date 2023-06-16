package io.com.bank.dto.member;

import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class MemberRequestDto {

    // 요청으로 받을 dto 객체 ( 패스워드 인코딩도 실시 )
    @Data
    public static class JoinRequestDto {
        private String username;
        private String password;
        private String email;
        private String fullname;

        public Member toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
            return Member.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(RoleEnum.CUSTOMER)
                    .build();
        }
    }
}
