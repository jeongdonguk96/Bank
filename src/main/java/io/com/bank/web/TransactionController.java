package io.com.bank.web;

import io.com.bank.auth.CustomUserDetails;
import io.com.bank.dto.ResponseDto;
import io.com.bank.dto.transaction.TransactionResponseDto.TransactionListResponseDto;
import io.com.bank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    // 계좌이체
    @PostMapping("/s/account/{number}/transactionHistory")
    public ResponseEntity<?> getTransactionHistory(@PathVariable Long number,
                                      @RequestParam(value = "gubun", defaultValue = "ALL") String gubun,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @AuthenticationPrincipal CustomUserDetails userDetails) {

        TransactionListResponseDto findTransactionHistories = transactionService.getTransactionHistory(userDetails.getMember().getId(), number, gubun, page);

//        return new ResponseEntity<>(new ResponseDto<>(1, "이체 목록 조회 완료", findTransactionHistories), HttpStatus.OK);
        return ResponseEntity.ok().body(new ResponseDto<>(1, "이체 목록 조회 완료", findTransactionHistories));
    }


}
