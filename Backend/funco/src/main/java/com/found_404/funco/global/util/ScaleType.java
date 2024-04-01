package com.found_404.funco.global.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ScaleType {
    VOLUME_SCALE(10),       // 코인 수량
    RETURN_RATE_SCALE(2),   // 수익률
    CASH_SCALE(0),          // 현금
    NORMAL_SCALE(30),       // 일반적인 연산의 경우
    ;

    private final int scale;
}
