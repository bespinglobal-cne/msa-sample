package com.msamaker.demo.domain;

import lombok.Getter;

@Getter
public class Payment {

//    @PositiveOrZero
    private Long money = 0L;

//    @PositiveOrZero
    private Long point = 0L;

    private Long price = 0L;
}
