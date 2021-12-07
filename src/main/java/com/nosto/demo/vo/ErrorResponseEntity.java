package com.nosto.demo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ErrorResponseEntity extends BaseResponseEntity {
    private String error;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @Setter
    private String path;

    public ErrorResponseEntity(int status, String error, String message) {
        super(status);
        setSuccess(false);
        this.error = error;
        this.message = message;
    }

}
