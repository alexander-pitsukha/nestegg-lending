package by.nestegg.lending.handler;

import by.nestegg.lending.exception.SocialException;
import by.nestegg.lending.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.UnexpectedTypeException;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String HANDLER = " handler.";

    @ExceptionHandler({UnexpectedTypeException.class, HttpClientErrorException.class, SocialException.class,
            NoSuchElementException.class})
    public ResponseEntity<Object> handleRuntimeException(RuntimeException e, WebRequest request) {
        log.info(e.getClass().getName() + HANDLER);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException e, WebRequest request) {
        log.info("UserNotFoundException" + HANDLER);
        return handleExceptionInternal(e, e.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

}
