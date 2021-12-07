package com.nosto.demo.configs;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosto.demo.common.ApiEndpoint;
import com.nosto.demo.common.exception.BusinessException;
import com.nosto.demo.vo.Rates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@Configuration
public class CacheConfiguration {

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected ApiEndpoint apiEndpoint;

    @Autowired
    protected ObjectMapper objectMapper;

    private static final String EXTERNAL_API_UNAVAILABLE = "The external exchange API temporarily unusable.";
    private static final String JSON_FORMAT_ERROR = "Json format error.";

    @DependsOn(value = {"restTemplate", "apiEndpoint", "objectMapper"})
    @Bean
    public LoadingCache<String, Rates> cache() {
        log.info("initialize cache component");
        return CacheBuilder.newBuilder()
                .initialCapacity(1)
                .concurrencyLevel(5)
                .expireAfterWrite(5, TimeUnit.SECONDS)
                .build(new CacheLoader<String, Rates>() {
                    @Override
                    public Rates load(String s) {
                        log.info("get rates from api.exchangeratesapi");
                        return getRates();
                    }
                });
    }

    private Rates getRates() {
        ResponseEntity<String> entity = restTemplate.getForEntity(apiEndpoint.getExchangeRateUrl(), String.class, apiEndpoint.getExchangeRateApiKey());

        if (entity.getStatusCode() != OK) {
            log.error(EXTERNAL_API_UNAVAILABLE);
            throw new BusinessException(EXTERNAL_API_UNAVAILABLE);
        }
        Rates rates;

        try {
            rates = objectMapper.readValue(entity.getBody(), Rates.class);
            rates.getRates().put("EUR", "1");
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(JSON_FORMAT_ERROR);
        }
        return rates;
    }
}
