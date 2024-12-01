package YandexPracticium.request;

import YandexPracticium.client.BaseClient;
import YandexPracticium.request.dto.ItemRequestDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(serverUrl, restTemplate);
    }

    public Object createRequest(Long userId, ItemRequestDto requestDto) {
        return post(API_PREFIX, userId, requestDto).getBody();
    }

    public Object getUserRequests(Long userId) {
        return get(API_PREFIX, userId).getBody();
    }

    public Object getAllRequests(Long userId, int from, int size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get(API_PREFIX + "/all?from={from}&size={size}", userId, parameters).getBody();
    }

    public Object getRequestById(Long userId, Long requestId) {
        return get(API_PREFIX + "/" + requestId, userId).getBody();
    }
}
