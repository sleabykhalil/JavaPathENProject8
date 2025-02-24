package tourGuide.exception.handlers;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import tourGuide.exception.TechnicalException;
import tourGuide.exception.ValidationException;

import java.util.Date;

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
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage validationExceptionHandler(ValidationException ex, WebRequest request) {
        return createValidationErrorMessage(ex, HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage feignExceptionHandler(TechnicalException ex, WebRequest request) {
        return createFeignExceptionErrorMessage(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    //All unknown errors unknown exception will be handled as TechnicalException
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
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

    private ErrorMessage createFeignExceptionErrorMessage(Throwable ex, HttpStatus httpStatus, WebRequest request) {
        log.error(starLine);
        log.error("* A connection exception occurred: {}[=type] {}[=message]", ex.getClass().getSimpleName(), ex.getMessage());
        log.error(fullStackTraceLog, ex);
        log.error(starLine);
        return createErrorMessage(ex, httpStatus, request);
    }


    private ErrorMessage createErrorMessage(Throwable e, HttpStatus httpStatus, WebRequest request) {
        return ErrorMessage.builder()
                .timestamp(new Date())
                .httpStatusCode(httpStatus.value())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build();
    }
}
