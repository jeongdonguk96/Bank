package io.com.bank.service;

import io.com.bank.domain.Member;
import io.com.bank.dto.member.MemberRequestDto.JoinRequestDto;
import io.com.bank.dto.member.MemberResponseDto.JoinResponseDto;
import io.com.bank.dummy.DummyObject;
import io.com.bank.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Mockito 확장 활성화 (Mockito 프레임워크 사용)
@ExtendWith(MockitoExtension.class)
public class MemberServiceTest extends DummyObject {

    @InjectMocks // 테스트할 클래스
    private MemberService memberService;

    @Mock // 테스트할 클래스에 필요한 의존관계 주입 (가짜 객체)
    private MemberRepository memberRepository;

    @Spy // 테스트할 클래스에 필요한 의존관계 주입 (실제 객체를 래핑한 스파이 객체)
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @DisplayName("회원가입 테스트")
    public void join_test() {
        // given
        JoinRequestDto joinRequestDto = JoinRequestDto.builder()
                                                .username("minsu")
                                                .password("1234")
                                                .email("minsu@gmail.com")
                                                .fullname("김민수")
                                                .build();

        // stub 1 : "username에 어느 데이터를 넣어도 빈 Optional 객체를 반환한다"
        when(memberRepository.findByUsername(any())).thenReturn(Optional.empty());
//        when(memberRepository.findByUsername(any())).thenReturn(Optional.of(new Member()));

        // stub 2 : "어느 Member 객체를 저장해도 donguk 객체가 저장된다"
        Member donguk = newMockMember(1L, "donguk", "정동욱");
        when(memberRepository.save(any())).thenReturn(donguk);

        // when
        JoinResponseDto joinResponseDto = memberService.join(joinRequestDto);
        System.out.println("joinResponseDto = " + joinResponseDto);

        // then
        assertThat(joinResponseDto.getId()).isEqualTo(1L);
        assertThat(joinResponseDto.getUsername()).isEqualTo("donguk");
        assertThat(joinResponseDto.getFullname()).isEqualTo("정동욱");
    }
}
