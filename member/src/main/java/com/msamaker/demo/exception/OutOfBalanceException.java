package com.msamaker.demo.exception;

public class OutOfBalanceException extends RuntimeException {
    public OutOfBalanceException() {
        super("한도 초과입니다.");
    }
}
