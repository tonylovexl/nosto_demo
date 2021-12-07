package com.nosto.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomizeErrorController extends BasicErrorController {

    public static final String MAP_KEY_SUCCESS = "success";
    public static final String MAP_KEY_TIMESTAMP = "timestamp";
    public static final String MAP_KEY_DATE = "date";
    public static final String MAP_KEY_STATUS = "status";
    public static final String MAP_KEY_ERROR = "error";
    public static final String MAP_KEY_MESSAGE = "message";
    public static final String MAP_KEY_PATH = "path";

    public CustomizeErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(), serverProperties.getError());
    }

    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
        HttpStatus status = getStatus(request);

        Map<String, Object> originalMsgMap = this.getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL));
        Map<String, Object> resultMap = new LinkedHashMap<>();

        resultMap.put(MAP_KEY_SUCCESS, false);
        resultMap.put(MAP_KEY_TIMESTAMP, originalMsgMap.get(MAP_KEY_TIMESTAMP));
        resultMap.put(MAP_KEY_DATE, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        resultMap.put(MAP_KEY_STATUS, originalMsgMap.get(MAP_KEY_STATUS));
        resultMap.put(MAP_KEY_ERROR, originalMsgMap.get(MAP_KEY_ERROR));
        resultMap.put(MAP_KEY_MESSAGE, status.toString());
        resultMap.put(MAP_KEY_PATH, originalMsgMap.get(MAP_KEY_PATH));

        return new ResponseEntity<>(resultMap, status);
    }
}
