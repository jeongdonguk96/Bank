package io.com.bank.service;

import io.com.bank.domain.Account;
import io.com.bank.domain.Transaction;
import io.com.bank.dto.transaction.TransactionResponseDto.TransactionListResponseDto;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;


    // 이체 내역 조회
    @Transactional
    public TransactionListResponseDto getTransactionHistory(Long memberId, Long accountNumber, String gubun, int page) {

        // 계좌 확인
        Account findAccount = accountRepository.findByNumber(accountNumber).orElseThrow(
                () -> new CustomApiException("해당 계좌를 찾을 수 없습니다")
        );

        // 계좌 소유자 확인
        findAccount.checkOwner(memberId);

        // 이체 목록 조회
        List<Transaction> transactions = transactionRepository.findTransactionList(findAccount.getId(), gubun, page);

        // 응답
        return new TransactionListResponseDto(findAccount, transactions);
    }
}
