package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.member.MemberRequestDto.JoinRequestDto;
import io.com.bank.dummy.DummyObject;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MemberControllerTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;


    @Test
    @DisplayName("회원가입 성공 테스트")
    void join_success_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                                                .username("minsu")
                                                .password("1234")
                                                .email("minsu@gmail.com")
                                                .fullname("김민수")
                                                .build();

        String requestBody = objectMapper.writeValueAsString(joinRequestDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/join").contentType(MediaType.APPLICATION_JSON).content(requestBody));

        // then
        resultActions.andExpect(status().isCreated());
    }

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
        dataSetting();
    }

    void dataSetting() {
        memberRepository.save(newMember("yongjoo", "이용주"));
    }

    @Test
    @DisplayName("회원가입 실패 테스트")
    void join_fail_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                .username("yongjoo")
                .password("1234")
                .email("yongjoo@gmail.com")
                .fullname("이용주")
                .build();

        String requestBody = objectMapper.writeValueAsString(joinRequestDto);

        // when
        ResultActions resultActions =
                mvc.perform(post("/api/join").contentType(MediaType.APPLICATION_JSON).content(requestBody));

        // then
        resultActions.andExpect(status().isBadRequest());
    }

}
