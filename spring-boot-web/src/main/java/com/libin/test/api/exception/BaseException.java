package com.libin.test.api.exception;

import lombok.Data;

/**
 * Created by libin on 2016/11/14.
 */
@Data
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 1441130070490530833L;

    protected String code;
    protected String message;

    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
