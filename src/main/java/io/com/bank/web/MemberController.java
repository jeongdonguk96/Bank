package io.com.bank.web;

import io.com.bank.dto.ResponseDto;
import io.com.bank.dto.member.MemberRequestDto.JoinRequestDto;
import io.com.bank.dto.member.MemberResponseDto.JoinResponseDto;
import io.com.bank.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Validated JoinRequestDto joinRequestDto, BindingResult bindingResult) {
        JoinResponseDto joinResponseDto = memberService.join(joinRequestDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResponseDto), HttpStatus.CREATED);
    }

}
