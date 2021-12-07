package com.nosto.demo.services;

import com.google.common.cache.LoadingCache;

import com.nosto.demo.vo.BaseResponseEntity;
import com.nosto.demo.vo.Rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ExecutionException;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Service
public class CurrencyService implements IService {

    private static final String SUCCESS_RESULT_FORMAT = "%s(%s) = %s(%s)";
    private static final String INVALID_PARAMETER_FORMAT = "Invalid parameter(s): %s";

    @Autowired
    private LoadingCache<String, Rates> cache;

    public ResponseEntity<? extends BaseResponseEntity> processCurrencyConvert(String from, String to, String amount) {
        Rates rates;
        try {
            rates = cache.get("rates");
        } catch (ExecutionException e) {
            return generateInteralErrorResponseEntity(e.getMessage());
        }

        from = from.toUpperCase(Locale.ROOT);
        to = to.toUpperCase(Locale.ROOT);

        try {
            validateCurrency(rates.getRates(), from, to, amount);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return generateParameterErrorResponseEntity(e.getMessage());
        }
        BigDecimal concreteRate = getConcreteRate(rates, from, to);

        String result = concreteRate.multiply(new BigDecimal(amount)).setScale(2, RoundingMode.HALF_UP).toString();

        return ResponseEntity.ok(generateSuccessResponseEntity(OK, String.format(SUCCESS_RESULT_FORMAT, amount, from, result, to)));
    }

    private BigDecimal getConcreteRate(Rates rates, String from, String to) {
        Map<String, String> ratesMap = rates.getRates();

        String fromRate = ratesMap.get(from);
        String toRate = ratesMap.get(to);

        return new BigDecimal(toRate).divide(new BigDecimal(fromRate), 6, RoundingMode.HALF_UP);
    }

    private void validateCurrency(Map<String, String> ratesMap, String from, String to, String amount) {
        StringJoiner sj = new StringJoiner(", ", "", "");
        if (isInvalidCurrency(ratesMap, from)) {
            sj.add(from);
        }
        if (isInvalidCurrency(ratesMap, to)) {
            sj.add(to);
        }
        try {
            Float.parseFloat(amount);
        } catch (NumberFormatException e) {
            sj.add(amount);
        }

        if (sj.length() > 0) {
            throw new IllegalArgumentException(String.format(INVALID_PARAMETER_FORMAT, sj));
        }
    }

    private boolean isInvalidCurrency(Map<String, String> ratesMap, String currency) {
        return !ratesMap.containsKey(currency);
    }
}
