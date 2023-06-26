package io.com.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dto.account.AccountResponseDto.AccountListResponseDto;
import io.com.bank.dto.account.AccountResponseDto.CreateResponseDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Transactional
@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks private AccountService accountService;
    @Mock private MemberRepository memberRepository;
    @Mock private AccountRepository accountRepository;
    @Spy private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 생성 및 목록 조회 테스트")
    void createAccount() throws Exception {

        // 계좌 생성
        // given
        Long memberId = 1L;
        CreateRequestDto createRequestDto = new CreateRequestDto();
        createRequestDto.setNumber(1111L);
        createRequestDto.setPassword(1234L);

        // stub1: 사용자 존재
        Member member = newMockMember(memberId, "donguk", "정동욱");
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        // stub2: 중복된 계좌 없음
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        // stub3: 계좌 생성 완료
        Account account = newMockAccount(1L, 1111L, 1000L, member);
        when(accountRepository.save(any())).thenReturn(account);

        // when
        CreateResponseDto createdAccount = accountService.createAccount(createRequestDto, memberId);
        String responseBody = objectMapper.writeValueAsString(createdAccount);
        System.out.println("responseBody = " + responseBody);

        // then
        assertThat(createdAccount.getNumber()).isEqualTo(1111L);

        // 계좌 목록 조회
        // when
        AccountListResponseDto accountList = accountService.getAccountList(member.getId());
        System.out.println("accountList = " + accountList);

        // then
        assertThat(accountList.getFullname()).isEqualTo("정동욱");
    }

}