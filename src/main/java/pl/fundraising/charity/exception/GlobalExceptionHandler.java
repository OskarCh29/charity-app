package pl.fundraising.charity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import pl.fundraising.charity.model.response.ErrorResponse;

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
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String errorMessage = e.getBindingResult().getAllErrors()
                .stream()
                .map(objectError -> objectError.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed - check request values");
        return new ResponseEntity<>(new ErrorResponse(status.value(), errorMessage), status);
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
    public ResponseEntity<?> handleTransferException(TransferException e) {
        HttpStatus status = HttpStatus.FORBIDDEN;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status.value(),
                e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status.value(),
                "Invalid input type - check your request"), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleExchangeClientException(MoneyExchangeClientException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status.value(), e.getMessage()), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status.value(), "Resource not found"), status);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(status.value(),
                "Invalid input - check your request"), status);
    }
}
