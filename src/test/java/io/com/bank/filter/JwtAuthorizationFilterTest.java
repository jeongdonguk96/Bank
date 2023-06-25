package io.com.bank.filter;

import io.com.bank.auth.CustomUserDetails;
import io.com.bank.domain.Member;
import io.com.bank.domain.RoleEnum;
import io.com.bank.dummy.DummyObject;
import io.com.bank.jwt.JwtProcess;
import io.com.bank.jwt.JwtVo;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthorizationFilterTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private MemberRepository memberRepository;


    @Test
    @DisplayName("ADMIN 인가 성공 테스트")
    void authorization_test1() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(RoleEnum.ADMIN).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String jwtToken = JwtProcess.create(userDetails);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello/test").header(JwtVo.HEADER, jwtToken));
        System.out.println("resultActions = " + resultActions.andReturn().getResponse().getStatus());

        // then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("ADMIN 인가 실패 테스트")
    void authorization_test2() throws Exception {
        // given
        Member member = Member.builder().id(1L).role(RoleEnum.CUSTOMER).build();
        CustomUserDetails userDetails = new CustomUserDetails(member);
        String jwtToken = JwtProcess.create(userDetails);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/hello/test").header(JwtVo.HEADER, jwtToken));
        System.out.println("resultActions = " + resultActions.andReturn().getResponse().getStatus());

        // then
        resultActions.andExpect(status().isForbidden());
    }
}