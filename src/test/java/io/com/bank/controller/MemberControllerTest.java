package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.member.MemberRequestDto.JoinRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class MemberControllerTest {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;


    @Test
    @DisplayName("회원가입 테스트")
    void join_test() throws Exception {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                                                .username("minsu")
                                                .password("1234")
                                                .email("minsu@gmail.com")
                                                .fullname("김민수")
                                                .build();

        String requestBody = objectMapper.writeValueAsString(joinRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
//        ResultActions resultActions = mvc.perform(post("/api/join").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        ResultActions resultActions = mvc.perform((RequestBuilder) post("/api/join").contextPath(requestBody).contentType(MediaType.APPLICATION_JSON));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then

    }

}
