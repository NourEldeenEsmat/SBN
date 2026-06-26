package com.nouresmat.book.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum BusinessErrorCodes {
    NO_CODES(0,"NO CODE",NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300,"INCORRECT PASSWORD",BAD_REQUEST),
    NEW_PASSWORD_DOSE_NOT_MATCH(301,"THE NEW PASSWORD DOSE NOT MATCH",BAD_REQUEST),
    ACCOUNT_LOCKED(302,"ACCOUNT IS LOCKED",FORBIDDEN),
    ACCOUNT_DISABLED(302,"ACCOUNT IS DISABLED",FORBIDDEN),
    BAD_CREDENTIAL(302,"EMAIL OR PASSWORD IS INCORRECT",FORBIDDEN),

    ;
    @Getter
    private final int Code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, String description, HttpStatus httpStatus) {
        Code = code;
        this.description = description;
        this.httpStatus = httpStatus;
    }


}
