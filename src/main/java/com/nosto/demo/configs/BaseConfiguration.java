package com.nosto.demo.configs;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nosto.demo.common.ApiEndpoint;
import com.nosto.demo.common.EnvironmentVariable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BaseConfiguration {

    private static final int CONNECT_TIMEOUT = 3000;
    private static final int READ_TIMEOUT = 5000;

    @Autowired
    private Environment environment;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT);
        requestFactory.setReadTimeout(READ_TIMEOUT);
        return requestFactory;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper()
                .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    }

    @Bean
    public ApiEndpoint apiEndpoint() {
        ApiEndpoint apiEndpoint = new ApiEndpoint();
        apiEndpoint.setExchangeRateApiKey(environment.getProperty(EnvironmentVariable.API_EXCHANGERATESAPI_KEY));
        String exchangeRateApiBaseEndpoint = environment.getProperty(EnvironmentVariable.API_EXCHANGERATESAPI_ENDPOINT);

        String exchangeRateApiUrl = exchangeRateApiBaseEndpoint + environment.getProperty(EnvironmentVariable.API_EXCHANGERATESAPI_LATEST_URL);
        apiEndpoint.setExchangeRateUrl(exchangeRateApiUrl);

        return apiEndpoint;
    }
}
