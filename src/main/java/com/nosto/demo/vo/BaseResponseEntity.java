package com.nosto.demo.vo;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class BaseResponseEntity {
    private boolean success;
    private Long timestamp;
    private String date;
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String costTime;

    public BaseResponseEntity(int status) {
        timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.status = status;
    }
}
