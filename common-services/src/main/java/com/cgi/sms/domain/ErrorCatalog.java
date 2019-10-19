package com.cgi.sms.domain;

import static com.cgi.sms.domain.ErrorType.FUNCTIONAL;
import static com.cgi.sms.domain.ErrorType.TECHNICAL;

public enum ErrorCatalog {

    APPLICATION_ERROR(1, "Something going wrong in the application...", FUNCTIONAL),
    BAD_DATA_ARGUMENT(2, "Service called with wrong arguments", FUNCTIONAL),
    DATA_BASE_ERROR(3, "Error while calling the database ", TECHNICAL),
    NO_RECORDS(204, "No Records matches the search criteria", FUNCTIONAL),

    REGISTRATION_NUMBER(308, "Error in Student Registration, Registration Number should be empty", FUNCTIONAL),
    DOESNOTEXIST(301, "Student Registration Does not Exist", FUNCTIONAL),
    STUDENT_RECORD_ALREDAY_EXISTS(106, "Student Record is already Exist", FUNCTIONAL);

    ErrorCatalog(int code, String message, ErrorType errorType) {
        this.code = code;
        this.message = message;
        this.errorType = errorType;
    }

    private int code;
    private String message;
    private ErrorType errorType;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

}
