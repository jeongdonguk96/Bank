package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.auth.CustomUserDetails;
import io.com.bank.domain.Member;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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
    @DisplayName("계좌 생성 및 목록 조회 테스트")
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

        // 계좌 목록 조회
        // when
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        OngoingStubbing<Optional<Member>> optionalOngoingStubbing = when(memberRepository.findById(any())).thenReturn(Optional.of(userDetails.getMember()));

        mvc.perform(post("/s/account/accountList").contentType(MediaType.APPLICATION_JSON).content(optionalOngoingStubbing));

        // then
    }
}
