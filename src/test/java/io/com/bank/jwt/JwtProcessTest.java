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
        Member member = Member.builder().id(1L).role(RoleEnum.CUSTOMER).build();
        CustomUserDetails userDetails_create = new CustomUserDetails(member);

        // when
        String jwtToken_full = JwtProcess.create(userDetails_create);
        String jwtToken_replaced = jwtToken_full.replace(JwtVo.TOKEN_PREFIX, "");

        // when
        CustomUserDetails userDetails_verify = JwtProcess.verify(jwtToken_replaced);

        // then
        assertEquals(1L, (long) userDetails_verify.getMember().getId());
        assertThat(userDetails_verify.getMember().getRole()).isEqualTo(RoleEnum.CUSTOMER);

    }
}
