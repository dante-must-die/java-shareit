package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Глобальный обработчик исключений.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
    public @ResponseBody ErrorResponse handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return new ErrorResponse("Conflict", ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ErrorResponse("Bad request", ex.getMessage());
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleNoSuchElementException(NoSuchElementException ex) {
        return new ErrorResponse("Not found", ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN) // 403 Forbidden
    public @ResponseBody ErrorResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ErrorResponse("Access denied", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ErrorResponse("Validation error", errorMessage);
    }

    @ExceptionHandler(InvalidCommentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleInvalidCommentException(InvalidCommentException ex) {
        return new ErrorResponse("Bad request", ex.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody ErrorResponse handleNotFoundException(final NotFoundException e) {
        return new ErrorResponse("NOT FOUND", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody ErrorResponse handleValidationException(final ValidationException e) {
        return new ErrorResponse("Bad request",e.getMessage());
    }
}


