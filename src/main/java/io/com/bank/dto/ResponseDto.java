package io.com.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseDto<T> {
    private final Integer code; // 1: 성공, -1: 실패
    private final String message;
    private final T data;
}
