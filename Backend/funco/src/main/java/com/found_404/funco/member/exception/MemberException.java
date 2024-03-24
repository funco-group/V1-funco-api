package com.found_404.funco.member.exception;

import com.found_404.funco.global.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MemberException extends BaseException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getErrorMsg());
    }
}
