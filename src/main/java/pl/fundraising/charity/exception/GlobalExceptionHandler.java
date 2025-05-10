package pl.fundraising.charity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import pl.fundraising.charity.model.response.ErrorResponse;
import pl.fundraising.charity.model.response.GeneralServerResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<?> handleEventAlreadyExistsException(EventAlreadyExistsException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleRecordNotFoundException(RecordNotFoundException e) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
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

    @ExceptionHandler
    public ResponseEntity<?> handleBoxAlreadyAssigned(BoxAlreadyAssignedException e) {
        HttpStatus status = HttpStatus.CONFLICT;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleDonationException(DonationException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        return new ResponseEntity<>(new ErrorResponse(status.value(),
                "Invalid input - check your request"), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleCantorClientException(CantorClientException e) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }
}
