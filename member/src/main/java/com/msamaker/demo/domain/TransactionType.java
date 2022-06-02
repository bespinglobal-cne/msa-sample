package com.msamaker.demo.domain;

/**
 * 보류
 */
public enum TransactionType {
    INCREASE(1), DECREASE(-1);

    private final int multiple;

    TransactionType(int multiple) {
        this.multiple = multiple;
    }

    public Long updateAmount(Long amount) {
        return amount * multiple;
    }
}
