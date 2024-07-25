package com.yongfill.server.domain.answer.exception;

import com.yongfill.server.global.common.response.error.ErrorCode;
import lombok.Getter;

@Getter
public class InterviewModeCustomException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Throwable cause;

    public InterviewModeCustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.cause = null;
    }

    public InterviewModeCustomException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
        this.cause = cause;
    }

}