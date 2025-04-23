package org.project1.shopweb.exception;

import lombok.Getter;

@Getter
public class DemoI18nException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object[] args; // i18n


    public DemoI18nException(ErrorCode errorCode, Object... args) {
        super(errorCode.getMessageKey());
        this.errorCode = errorCode;
        this.args = args;
    }
}



