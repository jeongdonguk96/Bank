package io.com.bank.dto.member;

import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

public class MemberRequestDto {

    // 요청으로 받을 dto 객체 ( 패스워드 인코딩도 실시 )
    @Data
    public static class JoinRequestDto {

        @Pattern(regexp = "", message = "영문/숫자로 4~20자 이내로 작성해주세요")
        @NotEmpty
        private String username;

        @NotEmpty
        private String password;

        @NotEmpty
        private String email;

        @NotEmpty
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
