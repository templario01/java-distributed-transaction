package templario01.io.authentication.adapter.input.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;
import templario01.io.authentication.domain.entity.DuplicatedEntityException;
import templario01.io.authentication.domain.entity.UnauthorizedEntityException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidation(WebExchangeBindException ex) {

        List<Map<String, Object>> errors = ex.getFieldErrors()
                .stream()
                .map(error -> {
                    Map<String, Object> err = new HashMap<>();
                    err.put("field", error.getField());
                    err.put("rejectedValue", error.getRejectedValue());
                    err.put("defaultMessage", error.getDefaultMessage());
                    return err;
                })
                .toList();

        Map<String, Object> body = new HashMap<>();
        body.put("errors", errors);
        body.put("status", 400);
        body.put("error", "Bad Request");

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body));
    }

    @ExceptionHandler(DuplicatedEntityException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleDuplicatedException(DuplicatedEntityException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("status", 400);
        body.put("error", "Bad Request");

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body));
    }

    @ExceptionHandler(UnauthorizedEntityException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleUnauthorizedException(UnauthorizedEntityException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("message", ex.getMessage());
        body.put("status", 401);
        body.put("error", "Unauthorized");

        return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body));
    }
}
