package jp.co.axa.apidemo.exception;

import org.springframework.http.HttpStatus;

public class EmployeeException {

    /**
     * Util class to create instances of CustomRuntimeException depending upon the parameters provided during construction of it.
     */
    private EmployeeException() {
        // don't allow to create any object.
    }

    public static CustomRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus, Throwable throwable) {
        return new CustomRuntimeException(errorMessage, httpStatus, throwable);
    }

    public static CustomRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus, String detailedErrorMessage) {
        return new CustomRuntimeException(errorMessage, httpStatus, detailedErrorMessage);
    }

    public static CustomRuntimeException prepareExceptionDetails(String errorMessage, HttpStatus httpStatus) {
        return new CustomRuntimeException(errorMessage, httpStatus);
    }
}
