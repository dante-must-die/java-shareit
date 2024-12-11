package expection;

import jakarta.validation.ValidationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exception.EmailAlreadyExistsException;
import ru.practicum.exception.InvalidCommentException;
import ru.practicum.exception.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/test-exceptions")
@Validated
class ExceptionThrowingController {

    @GetMapping("/email-exists")
    public void emailExists() {
        throw new EmailAlreadyExistsException("Email already exists");
    }

    @GetMapping("/illegal-argument")
    public void illegalArgument() {
        throw new IllegalArgumentException("Illegal argument");
    }

    @GetMapping("/no-such-element")
    public void noSuchElement() {
        throw new NoSuchElementException("No such element");
    }

    @GetMapping("/access-denied")
    public void accessDenied() throws AccessDeniedException {
        throw new AccessDeniedException("Access denied message");
    }

    @GetMapping("/invalid-comment")
    public void invalidComment() {
        throw new InvalidCommentException("Invalid comment");
    }

    @GetMapping("/not-found")
    public void notFound() {
        throw new NotFoundException("Not found something");
    }

    @GetMapping("/validation")
    public void validation() {
        throw new ValidationException("Validation error occurred");
    }

}
