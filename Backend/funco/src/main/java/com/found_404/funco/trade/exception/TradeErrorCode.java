package com.found_404.funco.trade.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum TradeErrorCode {
    INSUFFICIENT_COINS(HttpStatus.BAD_REQUEST, "자산이 충분하지 않습니다."),
    PRICE_CONNECTION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "시세 조회에 실패했습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String errorMsg;

}
