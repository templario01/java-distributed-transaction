package templario01.io.transaction.adapter.input.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(WebExchangeBindException ex) {

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

        Map<String, Object> response = new HashMap<>();
        response.put("errors", errors);
        response.put("error", "Bad Request");
        response.put("status", 400);


        return ResponseEntity.badRequest().body(response);
    }
}