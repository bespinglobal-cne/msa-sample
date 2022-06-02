package com.msamaker.demo.domain.dto;

import com.msamaker.demo.domain.TransactionType;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class TransactionDTO {

    private Long amount;
    private TransactionType type;
    private String notes;
}
