package jp.co.axa.apidemo.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jp.co.axa.apidemo.exception.model.ErrorResponse;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomRuntimeException extends RuntimeException {

    private static final long serialVersionUID = -531421314087148673L;

    private String errorMessage;
    private HttpStatus errorStatus;
    private String timeStamp;
    private String detailedErrorMessage;
    //used for field level validation details
    private ErrorResponse errorResponse;

    public CustomRuntimeException(String errorMessage, HttpStatus errorStatus) {
        this.errorMessage = errorMessage;
        this.errorStatus = errorStatus;
        this.timeStamp = getLocalTimeStamp();
    }

    public CustomRuntimeException(String errorMessage, HttpStatus errorStatus, String detailedErrorMessage) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = detailedErrorMessage;
    }

    public CustomRuntimeException(String errorMessage, HttpStatus errorStatus, ErrorResponse errorResponse) {
        this(errorMessage,errorStatus);
        this.errorResponse = errorResponse;
    }

    public CustomRuntimeException(String errorMessage, HttpStatus errorStatus, Throwable exception) {
        this(errorMessage,errorStatus);
        this.detailedErrorMessage = exception.getLocalizedMessage();
    }

    private String getLocalTimeStamp() {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now());
    }


}
