package io.com.bank.service;

import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;
    private final Logger log = LoggerFactory.getLogger(getClass());



}
