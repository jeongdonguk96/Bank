package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.domain.Member;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountControllerTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    @Autowired private AccountRepository accountRepository;


    @BeforeEach
    void setUp() {
        Member donguk = memberRepository.save(newMember("donguk", "정동욱"));
        accountRepository.save(newAccount(1111L, donguk));
    }


    @Test
    @DisplayName("계좌 생성 테스트")
    @WithUserDetails(value = "donguk", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void createAccount_test() throws Exception {
        // 계좌 생성
        // given
        CreateRequestDto createRequestDto = new CreateRequestDto();
        createRequestDto.setNumber(9999L);
        createRequestDto.setPassword(1234L);

        String requestBody = objectMapper.writeValueAsString(createRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/create").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isCreated());
    }


    @Test
    @DisplayName("계좌 삭제 성공 테스트")
    @WithUserDetails(value = "donguk", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteAccount_test_success() throws Exception {
        // given
        Long number = 1111L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/account/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
        assertThrows(CustomApiException.class, () -> accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다")
        ));
    }



    @Test
    @DisplayName("계좌 삭제 실패 테스트")
    @WithUserDetails(value = "donguk", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deleteAccount_test_fail() throws Exception {
        // given
        Long number = 2222L;

        // when
        ResultActions resultActions = mvc.perform(delete("/api/s/account/" + number));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isBadRequest());
    }
}
