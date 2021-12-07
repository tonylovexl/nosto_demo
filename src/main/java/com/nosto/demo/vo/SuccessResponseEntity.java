package com.nosto.demo.vo;

import lombok.Getter;

@Getter
public class SuccessResponseEntity extends BaseResponseEntity {

    private String result;

    public SuccessResponseEntity(int status, String result) {
        super(status);
        setSuccess(true);
        this.result = result;
    }
}
