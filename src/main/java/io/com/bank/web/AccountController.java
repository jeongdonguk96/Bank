package io.com.bank.web;

import io.com.bank.auth.CustomUserDetails;
import io.com.bank.dto.ResponseDto;
import io.com.bank.dto.account.AccountRequestDto;
import io.com.bank.dto.account.AccountRequestDto.CreateRequestDto;
import io.com.bank.dto.account.AccountRequestDto.DepositRequestDto;
import io.com.bank.dto.account.AccountResponseDto;
import io.com.bank.dto.account.AccountResponseDto.AccountListResponseDto;
import io.com.bank.dto.account.AccountResponseDto.CreateResponseDto;
import io.com.bank.dto.account.AccountResponseDto.DepositResponseDto;
import io.com.bank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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


    // 계좌 목록 조회
    @GetMapping("/s/account/accountList")
    public ResponseEntity<?> getAccountList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        AccountListResponseDto accountList = accountService.getAccountList(userDetails.getMember().getId());
        
        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 목록 조회 완료", accountList), HttpStatus.OK);
    }


    // 계좌 삭제
    @DeleteMapping("/s/account/{number}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long number, @AuthenticationPrincipal CustomUserDetails userDetails) {
        accountService.deleteAccount(number, userDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료", null), HttpStatus.OK);
    }
    
    
    // 입금
    @PostMapping("/account/deposit")
    public ResponseEntity<?> deposit(@RequestBody @Validated DepositRequestDto depositRequestDto,
                                     BindingResult bindingResult) {
        DepositResponseDto depositedAccount = accountService.deposit(depositRequestDto);

        return new ResponseEntity<>(new ResponseDto<>(1, "입금 완료", depositedAccount), HttpStatus.OK);
    }


    // 출금
    @PostMapping("/s/account/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody @Validated AccountRequestDto.WithdrawRequestDto withdrawRequestDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        AccountResponseDto.WithdrawResponseDto withdrawnAccount = accountService.withdraw(withdrawRequestDto, userDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "출금 완료", withdrawnAccount), HttpStatus.OK);
    }


    // 계좌이체
    @PostMapping("/s/account/transfer")
    public ResponseEntity<?> transfer(@RequestBody @Validated AccountRequestDto.TransferRequestDto transferRequestDto,
                                      BindingResult bindingResult,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        AccountResponseDto.TransferResponseDto transferResponseDto = accountService.transfer(transferRequestDto, userDetails.getMember().getId());

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌이체 완료", transferResponseDto), HttpStatus.OK);
    }


    // 계좌 상세 보기
    @GetMapping("/s/account/{number}")
    public ResponseEntity<?> getAccountDetail(@PathVariable Long number,
                                      @RequestParam(value = "page", defaultValue = "0") int page,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        AccountResponseDto.AccountDetailResponseDto findAccountDetail = accountService.getAccountDetail(number, userDetails.getMember().getId(), page);

        return new ResponseEntity<>(new ResponseDto<>(1, "계좌 상세 보기 완료", findAccountDetail), HttpStatus.OK);
    }

}
