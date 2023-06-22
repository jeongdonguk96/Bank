package io.com.bank.jwt;

import io.com.bank.auth.CustomUserDetails;
import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JwtProcessTest {

    @Test
    @DisplayName("JWT 생성 테스트")
    void create_test() {
        // given
        Member member = Member.builder().id(1L).role(RoleEnum.CUSTOMER).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);

        // when
        String jwtToken = JwtProcess.create(userDetails);
        System.out.println("jwtToken = " + jwtToken);

        // then
        assertTrue(jwtToken.startsWith("Bearer "));
        assertTrue(jwtToken.startsWith(JwtVo.TOKEN_PREFIX));
    }

    @Test
    @DisplayName("JWT 검증 테스트")
    void verify_test() {
        // given
        String jwtToken =
                "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiYW5rIiwicm9sZSI6IkNVU1RPTUVSIiwiaWQiOjEsImV4cCI6MTY4ODA0NTg0N30" +
                        ".k24oLscfiDzJe_lrvaqqWNTIgtkmlsxF9LjD5TyyBUBb4sJYq2jnw8mglAtDwkGc0lAWD7ATnDGaG4gSP6HiXw";

        // when
        CustomUserDetails userDetails = JwtProcess.verify(jwtToken);

        // then
        assertEquals(1L, (long) userDetails.getMember().getId());
        assertThat(userDetails.getMember().getRole()).isEqualTo(RoleEnum.CUSTOMER);

    }
}
