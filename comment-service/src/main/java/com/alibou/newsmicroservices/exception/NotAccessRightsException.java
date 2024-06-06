package com.alibou.newsmicroservices.exception;

import com.alibou.newsmicroservices.util.Constant;
import lombok.Getter;

@Getter
public class NotAccessRightsException extends RuntimeException{

    private Integer errorCode;

    public NotAccessRightsException(Integer errorCode) {
        super(Constant.AUTHOR_EXCEPTION);
        this.errorCode = errorCode;
    }

}