package com.nosto.demo.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosto.demo.common.ApiEndpoint;
import com.nosto.demo.vo.ErrorResponseEntity;
import com.nosto.demo.vo.SuccessResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public abstract class AbstractService {
    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected ApiEndpoint apiEndpoint;

    public ResponseEntity<ErrorResponseEntity> generateInteralErrorResponseEntity(String message) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(generateErrorResponseBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.toString(), message));
    }

    public ResponseEntity<ErrorResponseEntity> generateExternalApiErrorResponseEntity(String errorMsg) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(generateErrorResponseBody(HttpStatus.SERVICE_UNAVAILABLE.value(), HttpStatus.SERVICE_UNAVAILABLE.toString(), errorMsg));
    }

    public ResponseEntity<ErrorResponseEntity> generateParameterErrorResponseEntity(String errorMsg) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(generateErrorResponseBody(HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.toString(), errorMsg));
    }

    public ResponseEntity<ErrorResponseEntity> generateMethodNotAllowedResponseEntity(String errorMsg) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(generateErrorResponseBody(HttpStatus.METHOD_NOT_ALLOWED.value(), HttpStatus.METHOD_NOT_ALLOWED.toString(), errorMsg));
    }

    public SuccessResponseEntity generateSuccessResponseEntity(HttpStatus status, String result) {
        return new SuccessResponseEntity(status.value(), result);
    }

    public ErrorResponseEntity generateErrorResponseBody(int status, String error, String message) {
        return new ErrorResponseEntity(status, error, message);
    }
}
