package io.com.bank.dto.member;

import io.com.bank.domain.Member;
import io.com.bank.util.CustomDateUtil;
import lombok.Data;

public class MemberResponseDto {

    // 응답으로 보낼 로그인 dto 객체
    @Data
    public static class LoginResponseDto {
        private Long id;
        private String username;
        private String createdAt;

        public LoginResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.createdAt = CustomDateUtil.localDateTimeToString(member.getCreatedAt());
        }
    }

    // 응답으로 보낼 회원가입 dto 객체
    @Data
    public static class JoinResponseDto {
        private Long id;
        private String username;
        private String fullname;

        public JoinResponseDto(Member member) {
            this.id = member.getId();
            this.username = member.getUsername();
            this.fullname = member.getFullname();
        }
    }
}
