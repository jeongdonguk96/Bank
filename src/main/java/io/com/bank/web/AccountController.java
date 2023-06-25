package io.com.bank.web;

import io.com.bank.auth.CustomUserDetails;
import io.com.bank.dto.ResponseDto;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dto.account.AccountResponseDto.CreateResponseDto;
import io.com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;


    // 계좌 생성
    @PostMapping("/s/account/create")
    public ResponseEntity<?> createAccount(@RequestBody @Validated CreateRequestDto createRequestDto,
                                           BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails) {

        CreateResponseDto savedAccount = accountService.createAccount(createRequestDto, userDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 생성 완료", savedAccount), HttpStatus.CREATED);
    }


}
