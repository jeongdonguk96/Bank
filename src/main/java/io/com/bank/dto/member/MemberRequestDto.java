package io.com.bank.dto.member;

import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import lombok.Builder;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class MemberRequestDto {

    // 요청으로 받을 dto 객체 ( 패스워드 인코딩도 실시 )
    @Data
    @Builder
    public static class JoinRequestDto {

        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자로 2~20자 이내로 작성해주세요")
        @NotEmpty
        private String username;

        @Size(min = 4, max = 20)
        @NotEmpty
        private String password;

        @Pattern(regexp = "^^[a-zA-Z0-9]{2,20}+@[a-zA-Z0-9]{3,10}\\.[a-zA-Z]{2,5}$", message = "이메일 형식으로 작성해주세요")
        @NotEmpty
        private String email;

        @Pattern(regexp = "^[가-힣a-zA-Z0-9]{1,20}$", message = "한글/영문으로 1~20자 이내로 작성해주세요")
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
