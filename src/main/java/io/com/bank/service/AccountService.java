package io.com.bank.service;

import io.com.bank.domain.Account;
import io.com.bank.domain.Member;
import io.com.bank.domain.Transaction;
import io.com.bank.domain.TransactionEnum;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dto.account.AccountRequestDto.DepositRequestDto;
import io.com.bank.dto.account.AccountRequestDto.TransferRequestDto;
import io.com.bank.dto.account.AccountRequestDto.WithdrawRequestDto;
import io.com.bank.dto.account.AccountResponseDto;
import io.com.bank.dto.account.AccountResponseDto.TransferResponseDto;
import io.com.bank.dto.account.AccountResponseDto.AccountListResponseDto;
import io.com.bank.dto.account.AccountResponseDto.CreateResponseDto;
import io.com.bank.dto.account.AccountResponseDto.DepositResponseDto;
import io.com.bank.dto.account.AccountResponseDto.WithdrawResponseDto;
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
                .withdrawAccount(findAccount)
                .depositAccount(null)
                .withdrawAccountBalance(findAccount.getBalance())
                .depositAccountBalance(null)
                .amount(depositRequestDto.getAmount())
                .gubun(TransactionEnum.DEPOSIT)
                .sender("ATM")
                .receiver(depositRequestDto.getNumber().toString())
                .tel(depositRequestDto.getTel())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        return new DepositResponseDto(findAccount, savedTransaction);
    }

    // 출금
    @Transactional
    public WithdrawResponseDto withdraw(WithdrawRequestDto withdrawRequestDto, Long memberId) {
        // 100원 이하 체크
        if (withdrawRequestDto.getAmount() < 100L) {
            throw new CustomApiException("100원 미만의 금액은 출금할 수 없습니다.");
        }

        // 계좌 확인
        Account findAccount = accountRepository.findByNumber(withdrawRequestDto.getNumber()).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다")
        );

        // 춢금 소유자 확인
        findAccount.checkOwner(memberId);

        // 춢금 비밀번호 확인
        findAccount.checkPassword(withdrawRequestDto.getPassword());

        // 출금계좌 잔액 확인
//        findAccount.checkBalance(withdrawRequestDto.getAmount());

        // 출금
        findAccount.withdraw(withdrawRequestDto.getAmount());

        // 기록
        Transaction transaction = Transaction.builder()
                .withdrawAccount(findAccount)
                .depositAccount(null)
                .withdrawAccountBalance(findAccount.getBalance())
                .depositAccountBalance(null)
                .amount(withdrawRequestDto.getAmount())
                .gubun(TransactionEnum.WITHDRAW)
                .sender(String.valueOf(withdrawRequestDto.getNumber()))
                .receiver(withdrawRequestDto.getNumber().toString())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 응답
        return new WithdrawResponseDto(findAccount, savedTransaction);
    }

    // 계좌이체
    @Transactional
    public TransferResponseDto transfer(TransferRequestDto transferRequestDto, Long memberId) {
        // 출금/입금 계좌가 다른지 확인
        if (transferRequestDto.getWithdrawNumber().longValue() == transferRequestDto.getDepositNumber().longValue()) {
            throw new CustomApiException("동일한 계좌로 이체할 수 없습니다");
        }

        // 100원 이하 체크
        if (transferRequestDto.getAmount() < 100L) {
            throw new CustomApiException("100원 미만의 금액은 이체할 수 없습니다.");
        }

        // 출금 계좌 확인
        Account findWithdrawAccount = accountRepository.findByNumber(transferRequestDto.getWithdrawNumber()).orElseThrow(
                () -> new CustomApiException("출금할 계좌를 찾을 수 없습니다")
        );

        // 입금 계좌 확인
        Account findDepositAccount = accountRepository.findByNumber(transferRequestDto.getDepositNumber()).orElseThrow(
                () -> new CustomApiException("입금할 계좌를 찾을 수 없습니다")
        );

        // 춢금 소유자 확인
        findWithdrawAccount.checkOwner(memberId);

        // 춢금 비밀번호 확인
        findWithdrawAccount.checkPassword(transferRequestDto.getWithdrawPassword());

        // 출금계좌 잔액 확인
        findWithdrawAccount.checkBalance(transferRequestDto.getAmount());

        // 이체
        findWithdrawAccount.withdraw(transferRequestDto.getAmount());
        findDepositAccount.deposit(transferRequestDto.getAmount());

        // 기록
        Transaction transaction = Transaction.builder()
                .withdrawAccount(findWithdrawAccount)
                .depositAccount(findDepositAccount)
                .withdrawAccountBalance(findWithdrawAccount.getBalance())
                .depositAccountBalance(findDepositAccount.getBalance())
                .amount(transferRequestDto.getAmount())
                .gubun(TransactionEnum.TRANSFER)
                .sender(String.valueOf(findWithdrawAccount.getNumber()))
                .receiver(findDepositAccount.getNumber().toString())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);

        // 응답
        return new TransferResponseDto(findWithdrawAccount, savedTransaction);
    }

    // 계좌 상세 보기
    @Transactional
    public AccountResponseDto.AccountDetailResponseDto getAccountDetail(Long number, Long memberId, Integer page) {
        // 구분 값 고정
        String gubun = String.valueOf(TransactionEnum.ALL);

        // 계좌 확인
        Account findAccount = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("해당 계좌를 찾을 수 없습니다")
        );

        // 계좌 소유자 확인
        findAccount.checkOwner(memberId);

        // 이체 목록 조회
        List<Transaction> transactions = transactionRepository.findTransactionList(findAccount.getId(), gubun, page);

        // 응답
        return new AccountResponseDto.AccountDetailResponseDto(findAccount, transactions);
    }
}



















