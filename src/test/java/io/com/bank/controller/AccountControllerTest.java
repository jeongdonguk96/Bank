package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dto.account.AccountRequestDto;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import io.com.bank.repository.TransactionRepository;
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

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class AccountControllerTest extends DummyObject {

    @Autowired private MockMvc mvc;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private MemberRepository memberRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private EntityManager em;


    @BeforeEach
    void setUp() {
        dataSetting();
        em.clear();
    }


    @Test
    @DisplayName("계좌 생성 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
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


    @Test
    @DisplayName("입금 성공 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void deposit_test_success() throws Exception {
        // given
        AccountRequestDto.DepositRequestDto depositRequestDto = new AccountRequestDto.DepositRequestDto();
        depositRequestDto.setNumber(1111L);
        depositRequestDto.setAmount(1000L);
        depositRequestDto.setGubun(String.valueOf(TransactionEnum.DEPOSIT));
        depositRequestDto.setTel("01012345678");

        String requestBody = objectMapper.writeValueAsString(depositRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/account/deposit").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }


    @Test
    @DisplayName("출금 성공 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void withdraw_test_success() throws Exception {
        // given
        AccountRequestDto.WithdrawRequestDto withdrawRequestDto = new AccountRequestDto.WithdrawRequestDto();
        withdrawRequestDto.setNumber(1111L);
        withdrawRequestDto.setPassword(1234L);
        withdrawRequestDto.setAmount(100L);
        withdrawRequestDto.setGubun(String.valueOf(TransactionEnum.WITHDRAW));

        String requestBody = objectMapper.writeValueAsString(withdrawRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/withdraw").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }


    @Test
    @DisplayName("계좌이체 성공 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void transfer_test_success() throws Exception {
        // given
        AccountRequestDto.TransferRequestDto transferRequestDto = new AccountRequestDto.TransferRequestDto();
        transferRequestDto.setWithdrawNumber(1111L);
        transferRequestDto.setDepositNumber(2222L);
        transferRequestDto.setWithdrawPassword(1234L);
        transferRequestDto.setAmount(500L);
        transferRequestDto.setGubun(String.valueOf(TransactionEnum.TRANSFER));

        String requestBody = objectMapper.writeValueAsString(transferRequestDto);
        System.out.println("requestBody = " + requestBody);

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/transfer").contentType(MediaType.APPLICATION_JSON).content(requestBody));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("responseBody = " + responseBody);

        // then
        resultActions.andExpect(status().isOk());
    }


    @Test
    @DisplayName("계좌 상세 보기 성공 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void getAccountDetail_test_success() throws Exception {
            // given
            Long number = 1111L;
            String page = "0";

            // when
            ResultActions resultActions = mvc.perform(get("/api/s/account/" + number).param("page", page));
            String responseBody = resultActions.andReturn().getResponse().getContentAsString();
            System.out.println("responseBody = " + responseBody);

            // then
            resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
            resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
            resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
            resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
    }


    void dataSetting() {
        Member ssar = memberRepository.save(newMember("ssar", "쌀"));
        Member cos = memberRepository.save(newMember("cos", "코스,"));
        Member love = memberRepository.save(newMember("love", "러브"));
        Member admin = memberRepository.save(newMember("admin", "관리자"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(2222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }
}
