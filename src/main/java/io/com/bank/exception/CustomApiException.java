package io.com.bank.exception;

public class CustomApiException extends RuntimeException{

    public CustomApiException(String message) {
        super(message);
    }
}
