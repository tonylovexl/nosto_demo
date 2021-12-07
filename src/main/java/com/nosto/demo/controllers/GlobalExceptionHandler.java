package com.nosto.demo.controllers;

import com.nosto.demo.services.IService;
import com.nosto.demo.vo.ErrorResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private IService service;

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseEntity> handleBusinessException(Exception e, HttpServletRequest req) {
        log.error(e.getMessage(), e);
        ResponseEntity<ErrorResponseEntity> responseEntity = service.generateParameterErrorResponseEntity(e.getMessage());
        responseEntity.getBody().setPath(req.getContextPath() + req.getServletPath());

        return responseEntity;
    }
}
