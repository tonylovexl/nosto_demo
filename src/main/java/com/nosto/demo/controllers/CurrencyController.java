package com.nosto.demo.controllers;

import com.nosto.demo.common.aspect.CalculationTimeStatistic;
import com.nosto.demo.services.CurrencyService;
import com.nosto.demo.vo.BaseResponseEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("v1/currency")
public class CurrencyController extends AbstractController {

    @Autowired
    private CurrencyService currencyService;

    @CalculationTimeStatistic
    @GetMapping("convert")
    public ResponseEntity<? extends BaseResponseEntity> convert(@RequestParam String from,
                                                                @RequestParam String to,
                                                                @RequestParam String amount) {
        return currencyService.processCurrencyConvert(from, to, amount);
    }

    @CalculationTimeStatistic
    @RequestMapping(value = "convert", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH, RequestMethod.DELETE})
    public ResponseEntity<? extends BaseResponseEntity> handleInValidConvert(@RequestParam(required = false) String from,
                                                                             @RequestParam(required = false) String to,
                                                                             @RequestParam(required = false) String amount) {
        return currencyService.generateMethodNotAllowedResponseEntity("Convert api is only accessed by GET method");
    }

}
