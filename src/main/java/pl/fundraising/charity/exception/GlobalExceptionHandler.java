package pl.fundraising.charity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.fundraising.charity.model.response.GeneralServerResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleEventAlreadyExistsException(EventAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed - check request values");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new GeneralServerResponse(errorMessage)
        );
    }
}
