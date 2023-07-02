package io.com.bank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dummy.DummyObject;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import io.com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@Sql("classpath:db/teardown.sql")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class TransactionControllerTest extends DummyObject {

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
    @DisplayName("이체 목록 조회 테스트")
    @WithUserDetails(value = "ssar", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void findTransactionList_test() throws Exception {
        // given
        Long number = 1111L;
        String gubun = String.valueOf(TransactionEnum.ALL);
        String page = "0";

        // when
        ResultActions resultActions = mvc.perform(post("/api/s/account/" + number + "/transactionHistory").param("gubun", gubun).param("page", page));
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
