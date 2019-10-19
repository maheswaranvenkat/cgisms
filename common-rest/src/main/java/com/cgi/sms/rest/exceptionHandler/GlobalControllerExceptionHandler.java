package com.cgi.sms.rest.exceptionHandler;

import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.rest.dto.ErrorDto;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDto> handleServiceException(ServiceException serviceException) {
        return ResponseEntity.status(getStatus(serviceException))
                .body(getBuildErrorDto(serviceException));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException illegalArgumentException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorDto(ErrorCatalog.BAD_DATA_ARGUMENT.getCode(), illegalArgumentException.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentException(MethodArgumentNotValidException m) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorDto(ErrorCatalog.BAD_DATA_ARGUMENT.getCode(), m.getBindingResult().getFieldError().getDefaultMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstrainViolationException(ConstraintViolationException c) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value())
                .body(new ErrorDto(ErrorCatalog.BAD_DATA_ARGUMENT.getCode(), c.getMessage()));
    }

    private ErrorDto getBuildErrorDto(ServiceException serviceException) {
        int code = serviceException.getErrorCatalog().getCode();
        String message = serviceException.getMessage();
        return new ErrorDto(code, message);
    }

    private int getStatus(ServiceException e) {
        switch (e.getErrorCatalog()) {
            case BAD_DATA_ARGUMENT:
                return HttpStatus.BAD_REQUEST.value();
            case DATA_BASE_ERROR:
                return HttpStatus.SERVICE_UNAVAILABLE.value();
            case APPLICATION_ERROR:
                return HttpStatus.BAD_REQUEST.value();
            case NO_RECORDS:
                return HttpStatus.NO_CONTENT.value();
            default:
                return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }
    }
}
