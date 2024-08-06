package com.microservice.playlist.client;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;

@Configuration
public class FeignConfig {

    @Bean
    public SpringEncoder feignEncoder() {
        return new SpringEncoder(() -> {
            HttpMessageConverters converters = new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
            return converters;
        });
    }

    @Bean
    public SpringDecoder feignDecoder() {
        return new SpringDecoder(() -> {
            HttpMessageConverters converters = new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
            return converters;
        });
    }
}
