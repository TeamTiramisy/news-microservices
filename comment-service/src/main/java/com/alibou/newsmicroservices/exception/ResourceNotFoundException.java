package com.alibou.newsmicroservices.exception;

import com.alibou.newsmicroservices.util.Constant;
import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException{

    private String fieldName;

    private Object fieldValue;

    private Integer errorCode;

    public ResourceNotFoundException(String fieldName, Object fieldValue, Integer errorCode) {
        super(String.format(Constant.EXCEPTION_MESSAGE, fieldName, fieldValue));
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.errorCode = errorCode;
    }

}
