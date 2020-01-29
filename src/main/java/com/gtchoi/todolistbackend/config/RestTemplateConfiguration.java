package com.gtchoi.todolistbackend.config;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public FormHttpMessageConverter formHttpMessageConverter() {
        return new FormHttpMessageConverter();

    }

    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder, FormHttpMessageConverter formHttpMessageConverter) {
        return builder.detectRequestFactory(true)
                .defaultMessageConverters()
//                .additionalMessageConverters(formHttpMessageConverter)
                .build();
    }

    @Bean
    public ClientHttpRequestFactory requestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                                                        .setMaxConnPerRoute(3)
                                                        .setMaxConnTotal(10)
                                                        .build();
        factory.setHttpClient(httpClient);
        return factory;

    }
}
