package io.com.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dto.account.AccountRequestDto.DepositRequestDto;
import io.com.bank.dto.account.AccountResponseDto.CreateResponseDto;
import io.com.bank.dto.account.AccountResponseDto.DepositResponseDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import io.com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
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
    @Mock private TransactionRepository transactionRepository;
    @Spy private ObjectMapper objectMapper;

    @Test
    @DisplayName("계좌 생성 테스트")
    void createAccount_test() throws Exception {
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
    }

    @Test
    @DisplayName("계좌 삭제 성공 테스트")
    void deleteAccount_success_test() throws Exception {
        // given
        Long number = 1111L;
        Long memberId = 1L;

        // stub1: 사용자, 계좌 생성
        Member member = newMockMember(1L, "donguk", "정동욱");
        Account account = newMockAccount(1L, 111L, 1000L, member);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        // then
        accountService.deleteAccount(number, memberId);
    }

    @Test
    @DisplayName("계좌 삭제 실패 테스트")
    void deleteAccount_fail_test() throws Exception {
        // given
        Long number = 1111L;
        Long memberId = 2L;

        // stub1: 사용자, 계좌 생성
        Member member = newMockMember(1L, "donguk", "정동욱");
        Account account = newMockAccount(1L, 111L, 1000L, member);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        // then
        Assertions.assertThrows(CustomApiException.class, () -> accountService.deleteAccount(number, memberId));
    }

    @Test
    @DisplayName("입금 성공 테스트")
    void deposit_success_test() throws Exception {
        // given
        // stub1: 사용자, 계좌 생성
        Member member = newMockMember(1L, "donguk", "정동욱");
        Account account = newMockAccount(1L, 1111L, 1000L, member);

        DepositRequestDto depositRequestDto = new DepositRequestDto();
        depositRequestDto.setNumber(account.getNumber());
        depositRequestDto.setAmount(1000L);
        depositRequestDto.setGubun(String.valueOf(TransactionEnum.DEPOSIT));
        depositRequestDto.setTel("01012345678");

        // stub2: 입금계좌 존재
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(account));

        // stub3: 트랜잭션 존재
        Transaction transaction = newMockDepositTransaction(1L, account);
        when(transactionRepository.save(any())).thenReturn(transaction);

        // when
        DepositResponseDto depositResponseDto = accountService.deposit(depositRequestDto);
        String responseBody = objectMapper.writeValueAsString(depositResponseDto);
        System.out.println("responseBody = " + responseBody);

        // then
        assertThat(depositResponseDto.getNumber()).isEqualTo(1111L);
    }
}