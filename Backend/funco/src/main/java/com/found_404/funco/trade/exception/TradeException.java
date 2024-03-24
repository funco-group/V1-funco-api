package com.found_404.funco.trade.exception;

import com.found_404.funco.global.exception.BaseException;
import lombok.Getter;

@Getter
public class TradeException extends BaseException {
    public TradeException(TradeErrorCode errorCode) {
        super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getErrorMsg());
    }
}
