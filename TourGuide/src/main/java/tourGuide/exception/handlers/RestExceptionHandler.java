package tourGuide.exception.handlers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import tourGuide.exception.TechnicalException;
import tourGuide.exception.ValidationException;

import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler {
    private static String starLine = "*****************************************************************************************************";
    private static String fullStackTraceLog = "* Full stack trace";

    @ExceptionHandler(TechnicalException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage technicalExceptionHandler(TechnicalException ex, WebRequest request) {
        return createTechnicalErrorMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage validationExceptionHandler(ValidationException ex, WebRequest request) {
        return createValidationErrorMessage(ex, HttpStatus.BAD_REQUEST, request);
    }

    //All unknown errors unknown exception will be handled as TechnicalException
    @ExceptionHandler(Throwable.class)
    public ErrorMessage globalExceptionHandler(TechnicalException ex, WebRequest request) {
        return createTechnicalErrorMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ErrorMessage createTechnicalErrorMessage(Throwable ex, HttpStatus httpStatus, WebRequest request) {
        log.error(starLine);
        log.error("* A technical exception occurred: {}[=type] {}[=message]", ex.getClass().getSimpleName(), ex.getMessage());
        log.error(fullStackTraceLog, ex);
        log.error(starLine);
        return createErrorMessage(ex, httpStatus, request);
    }

    private ErrorMessage createValidationErrorMessage(Throwable ex, HttpStatus httpStatus, WebRequest request) {
        log.error(starLine);
        log.error("* A Validation exception occurred: {}[=type] {}[=message]", ex.getClass().getSimpleName(), ex.getMessage());
        log.error(fullStackTraceLog, ex);
        log.error(starLine);
        return createErrorMessage(ex, httpStatus, request);
    }


    private ErrorMessage createErrorMessage(Throwable e, HttpStatus httpStatus, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(now())
                .httpStatusCode(httpStatus.value())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build();
    }
}
