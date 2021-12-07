package com.nosto.demo.vo;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class SuccessResponseEntity extends BaseResponseEntity {

    private String result;

    public SuccessResponseEntity(int status, String result) {
        super(status);
        setSuccess(true);
        this.result = result;
    }
}
