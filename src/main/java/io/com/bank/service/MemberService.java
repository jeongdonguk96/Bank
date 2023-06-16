package io.com.bank.service;

import io.com.bank.domain.Member;
import io.com.bank.dto.member.MemberRequestDto.JoinRequestDto;
import io.com.bank.dto.member.MemberResponseDto.JoinResponseDto;
import io.com.bank.exception.CustomApiException;
import io.com.bank.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Logger log = LoggerFactory.getLogger(getClass());

    // 회원가입
    @Transactional
    public JoinResponseDto join(JoinRequestDto joinRequestDto) {
        
        // 1. 아이디 중복확인
        Optional<Member> findOptionalMember = memberRepository.findByUsername(joinRequestDto.getUsername());

        if (findOptionalMember.isPresent()) {
            throw new CustomApiException("동일한 아이디가 존재합니다");
        }

        // 2. 패스워드 인코딩 + 회원가입
        Member member = memberRepository.save(joinRequestDto.toEntity(bCryptPasswordEncoder));

        // dto 객체로 응답
        return new JoinResponseDto(member);
    }

}
