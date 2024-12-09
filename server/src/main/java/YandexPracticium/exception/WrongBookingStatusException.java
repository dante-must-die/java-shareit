package YandexPracticium.exception;

public class WrongBookingStatusException extends RuntimeException {
    public WrongBookingStatusException(String message) {
        super(message);
    }
}
