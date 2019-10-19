package com.cgi.sms.exception;

import com.cgi.sms.domain.ErrorCatalog;
import com.cgi.sms.domain.ErrorOrigin;

public class ServiceException extends RuntimeException {
    private ErrorOrigin errorOrigin;
    private ErrorCatalog errorCatalog;

    public ServiceException(ErrorCatalog errorCatalog, ErrorOrigin errorOrigin) {
        this(errorCatalog, errorOrigin, null, null);
    }

    public ServiceException(ErrorCatalog errorCatalog, ErrorOrigin errorOrigin, Throwable throwable) {
        this(errorCatalog, errorOrigin, null, throwable);
    }

    public ServiceException(ErrorCatalog errorCatalog, ErrorOrigin errorOrigin, String message) {
        this(errorCatalog, errorOrigin, message, null);
    }

    public ServiceException(ErrorCatalog errorCatalog, ErrorOrigin errorOrigin, String message, Throwable throwable) {
        super(message, throwable);
        this.errorCatalog = errorCatalog;
        this.errorOrigin = errorOrigin;
    }

    public ErrorOrigin getErrorOrigin() { return errorOrigin; }

    public ErrorCatalog getErrorCatalog() { return errorCatalog; }
}
