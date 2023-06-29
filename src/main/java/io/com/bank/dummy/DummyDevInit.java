package io.com.bank.dummy;

import io.com.bank.domain.Member;
import io.com.bank.repository.AccountRepository;
import io.com.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class DummyDevInit extends DummyObject{

    private final MemberRepository memberRepository;
    private final AccountRepository accountRepository;

    @Profile("dev")
    @PostConstruct
    void init() {
        Member donguk = memberRepository.save(newMember("donguk", "정동욱"));
        accountRepository.save(newAccount(1111L, donguk));
        System.out.println("DummyDataInit");
    }
}

//@Configuration
//public class DummyDevInit extends DummyObject {
//
//    @Bean
//    @Profile("dev")
//    CommandLineRunner init(MemberRepository memberRepository) {
//        return (args) -> {
//            memberRepository.save(newMember("donguk", "정동욱"));
//        };
//    }
//}

