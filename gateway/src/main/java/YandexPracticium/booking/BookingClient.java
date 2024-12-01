package YandexPracticium.booking;

import YandexPracticium.booking.dto.BookingDto;
import YandexPracticium.client.BaseClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

@Component
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplate restTemplate) {
        super(serverUrl, restTemplate);
    }

    public ResponseEntity<Object> createBooking(Long userId, BookingDto bookingDto) {
        return post(API_PREFIX, userId, bookingDto);
    }

    public ResponseEntity<Object> approveBooking(Long userId, Long bookingId, Boolean approved) {
        String path = API_PREFIX + "/" + bookingId + "?approved=" + approved;
        return patch(path, userId, null);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return get(API_PREFIX + "/" + bookingId, userId);
    }

    public ResponseEntity<Object> getBookings(Long userId, String state) {
        String path = API_PREFIX + "?state=" + state;
        return get(path, userId);
    }

    public ResponseEntity<Object> getBookingsForOwner(Long userId, String state) {
        String path = API_PREFIX + "/owner?state=" + state;
        return get(path, userId);
    }
}
