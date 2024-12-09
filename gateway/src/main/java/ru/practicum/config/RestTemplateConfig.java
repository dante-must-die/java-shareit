package ru.practicum.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.util.Timeout;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    public RestTemplate restTemplate() {
        // Создаем RequestConfig с таймаутами
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(Timeout.ofSeconds(10))
                .setResponseTimeout(Timeout.ofSeconds(10))
                .build();

        // Создаем HttpClient с конфигурацией
        var httpClient = HttpClients.custom()
                .setDefaultRequestConfig(config)
                .build();

        // Создаем фабрику с HttpClient
        var requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);

        return new RestTemplate(requestFactory);
    }
}
