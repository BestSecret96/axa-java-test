package jp.co.axa.apidemo.exception.handler;

import jp.co.axa.apidemo.exception.CustomRuntimeException;
import jp.co.axa.apidemo.exception.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    final Logger logger = LoggerFactory.getLogger(CustomExceptionHandler.class);

    //define all the different types of exception your employee service can throw. Then accordingly add the custom handling code.

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.debug("Triggering exception handler for HttpMessageNotReadableException type.");
        String error = "Malformed JSON request. Please check your request body.";
        return buildResponseEntity(new CustomRuntimeException(error, HttpStatus.BAD_REQUEST, ex.getLocalizedMessage()), webRequest);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest webRequest) {
        logger.debug("Triggering exception handler for MethodArgumentNotValidException type.");
        String error = "Validation failed for given Payload. Please check your request input.";
        return buildResponseEntity(new CustomRuntimeException(error, HttpStatus.BAD_REQUEST, ex.getBindingResult().toString()), webRequest);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException exception, WebRequest webRequest) {
        logger.debug("Triggering exception handler for EntityNotFoundException type.");
        return buildResponseEntity(new CustomRuntimeException(exception.getMessage(), HttpStatus.NOT_FOUND), webRequest);
    }

    @ExceptionHandler(value = {CustomRuntimeException.class})
    protected ResponseEntity<Object> handleApiRuntimeException(CustomRuntimeException exception, WebRequest webRequest) {
        logger.debug("Triggering exception handler for CustomRuntimeException type.");
        return buildResponseEntity(exception, webRequest);
    }

    private ResponseEntity<Object> buildResponseEntity(CustomRuntimeException customRuntimeException, WebRequest webRequest) {
        ErrorResponse response = new ErrorResponse(customRuntimeException.getErrorMessage(), customRuntimeException.getErrorStatus().value(),
                customRuntimeException.getTimeStamp(), customRuntimeException.getDetailedErrorMessage(),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI());
        return new ResponseEntity<>(response, customRuntimeException.getErrorStatus());
    }
}
