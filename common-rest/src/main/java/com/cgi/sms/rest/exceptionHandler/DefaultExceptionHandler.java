package com.cgi.sms.rest.exceptionHandler;

import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.exception.ServiceException;
import com.cgi.sms.rest.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorDto> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto(ErrorCatalog.APPLICATION_ERROR.getCode(), ErrorCatalog.APPLICATION_ERROR.getMessage()));
    }
}
