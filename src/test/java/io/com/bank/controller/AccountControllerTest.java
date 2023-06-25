package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountControllerTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;


//    @BeforeEach
//    void setUp() {
//        dataSetting();
//    }
//
//    void dataSetting() {
//        List<Member> all = memberRepository.findAll();
//        System.out.println("before deleteAll list size = " + all.size());
//        memberRepository.deleteAll();
//        memberRepository.save(newMember("donguk", "정동욱"));
//
//        List<Member> all2 = memberRepository.findAll();
//        System.out.println("after deleteAll list size = " + all2.size());
//    }

    @Test
    @WithUserDetails(value = "donguk", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("계좌 생성 테스트")
    void createAccount_test() throws Exception {
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
}
