package tourGuide.exception;


import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message, null, false, false);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValidationException(Throwable cause) {
        super(cause);
    }
}
