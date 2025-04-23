package org.project1.shopweb.exception;

import lombok.Getter;
import org.project1.shopweb.utils.MessageKeys;

@Getter
public enum ErrorCode {

    // === USER ===
    LOGIN_FAILED(1000, MessageKeys.LOGIN_FAILED),
    PASSWORD_NOT_MATCH(1001, MessageKeys.PASSWORD_NOT_MATCH),
    REGISTER_FAILED(1002, MessageKeys.REGISTER_FAILED),
    ROLE_NOT_FOUND(1003, MessageKeys.ROLE_NOT_FOUND),
    USER_IS_LOCKED(1004, MessageKeys.USER_IS_lOCKED),
    WRONG_USER_PASS(1005, MessageKeys.WRONG_USER_PASS),

    // === CATEGORY ===
    CATEGORY_CREATE_FAIL(2000, MessageKeys.CATEGORY_CREAT_FAIL),

    // === PRODUCT / IMAGE ===
    MAX_IMAGE_LIMIT(3000, MessageKeys.MAX_5_IMAGE),
    IMAGE_TOO_LARGE(3001, MessageKeys.MAX_MB),
    FILE_MUST_BE_IMAGE(3002, MessageKeys.MUST_IMG),

    // === ORDER ===
    DELETE_ORDER_SUCCESS(4000, MessageKeys.DELETE_ORDER),
    DELETE_ORDER_DETAIL_SUCCESS(4001, MessageKeys.DELETE_ORDER_DETAIL);

    private final int code;
    private final String messageKey;

    ErrorCode(int code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }
}
