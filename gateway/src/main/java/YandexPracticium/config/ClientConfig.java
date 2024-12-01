package YandexPracticium.config;

import YandexPracticium.booking.BookingClient;
import YandexPracticium.item.ItemClient;
import YandexPracticium.request.ItemRequestClient;
import YandexPracticium.user.UserClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

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
