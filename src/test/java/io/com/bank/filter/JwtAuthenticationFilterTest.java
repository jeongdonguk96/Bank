package io.com.bank.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.member.MemberRequestDto.LoginRequestDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.jwt.JwtVo;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class JwtAuthenticationFilterTest extends DummyObject {

    @Autowired private ObjectMapper objectMapper;
    @Autowired private MockMvc mvc;
    @Autowired private MemberRepository memberRepository;


    @BeforeEach
    @DisplayName("테스트 전 더미 멤버 객체 생성")
    void setUp_test() {
        memberRepository.save(newMember("donguk", "정동욱"));
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void successfulAuthentication_test() throws Exception {
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("donguk");
        loginRequestDto.setPassword("1234");

        String requestBody = objectMapper.writeValueAsString(loginRequestDto);

        // when
        ResultActions resultActions = mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVo.HEADER);

        // then
        resultActions.andExpect(status().isOk());
        assertNotNull(jwtToken);
        assertTrue(jwtToken.startsWith(JwtVo.TOKEN_PREFIX));
        resultActions.andExpect(jsonPath("$.data.username").value("donguk"));
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void unsuccessfulAuthentication_test() throws Exception{
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setUsername("");
        loginRequestDto.setPassword("1234");

        String requestBody = objectMapper.writeValueAsString(loginRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
        mvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(requestBody)).andExpect(status().isUnauthorized());

    }
}