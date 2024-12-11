package ru.practicum.config;

import ru.practicum.booking.BookingClient;
import ru.practicum.item.ItemClient;
import ru.practicum.request.ItemRequestClient;
import ru.practicum.user.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурация клиентов для взаимодействия с сервером ShareIt.
 */
@Configuration
public class ClientConfig {

    @Value("${shareit-server.url}")
    private String serverUrl;

    @Bean
    public UserClient userClient(RestTemplate restTemplate) {
        return new UserClient(serverUrl, restTemplate);
    }

    @Bean
    public ItemClient itemClient(RestTemplate restTemplate) {
        return new ItemClient(serverUrl, restTemplate);
    }

    @Bean
    public BookingClient bookingClient(RestTemplate restTemplate) {
        return new BookingClient(serverUrl, restTemplate);
    }

    @Bean
    public ItemRequestClient itemRequestClient(RestTemplate restTemplate) {
        return new ItemRequestClient(serverUrl, restTemplate);
    }
}
