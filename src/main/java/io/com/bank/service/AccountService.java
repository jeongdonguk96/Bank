package io.com.bank.service;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dto.account.AccountRequestDto.*;
import io.com.bank.dto.account.AccountResponseDto.*;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import io.com.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());


    // 계좌 생성
    @Transactional
    public CreateResponseDto createAccount(CreateRequestDto createRequestDto, Long memberId) {

        // 사용자 존재 여부 확인
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                ()-> new CustomApiException("사용자를 찾을 수 없습니다")
        );

        // 중복 계좌 존재 여부 확인
        Optional<Account> findAccount = accountRepository.findByNumber(createRequestDto.getNumber());
        if (findAccount.isPresent()) {
            throw new CustomApiException("이미 존재하는 계좌입니다.");
        }

        // 계좌 생성
        Account savedAccount = accountRepository.save(createRequestDto.toEntity(findMember));

        // Dto로 응답
        return new CreateResponseDto(savedAccount);
    }


    // 계좌 목록 조회
    @Transactional(readOnly = true)
    public AccountListResponseDto getAccountList(Long memberId) {

        // 사용자 존재 여부 확인
        Member findMember = memberRepository.findById(memberId).orElseThrow(
                () -> new CustomApiException("사용자를 찾을 수 없습니다")
        );

        // 계좌 목록 조회
        List<Account> findAccountList = accountRepository.findAllByMemberId(memberId);

        // Dto로 응답
        return new AccountListResponseDto(findMember, findAccountList);
    }


    // 계좌 삭제
    @Transactional
    public void deleteAccount(Long number, Long memberId) {
        // 계좌 확인
        Account findAccount = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다")
        );

        // 계좌 소유자 확인
        findAccount.checkOwner(memberId);

        // 계좌 삭제
        accountRepository.deleteById(findAccount.getId());
    }


    // 입금
    @Transactional
    public DepositResponseDto deposit(DepositRequestDto depositRequestDto) {
        // 입금액이 0원인지 확인
        if (depositRequestDto.getAmount() < 100L) {
            throw new CustomApiException("100원 미만의 금액은 입금할 수 없습니다.");
        }

        // 입금 계좌가 있는지 확인
        Account findAccount = accountRepository.findByNumber(depositRequestDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다")
        );

        // 입금
        findAccount.deposit(depositRequestDto.getAmount());

        // 거래내역 남기기
        Transaction transaction = Transaction.builder()
                .depositAccount(findAccount)
                .withdrawAccount(null)
                .depositAccountBalance(findAccount.getBalance())
                .withdrawAccountBalance(null)
                .amount(depositRequestDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(depositRequestDto.getNumber().toString())
                .tel(depositRequestDto.getTel())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new DepositResponseDto(findAccount, savedTransaction);
    }

}



















