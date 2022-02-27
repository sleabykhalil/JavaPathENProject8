package tourGuide.exception;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class TechnicalException extends RuntimeException {

    public TechnicalException(String message) {
        super(message, null, false, false);
    }

    public TechnicalException(String message, Throwable cause) {
        super(message, cause);
    }

    public TechnicalException(Throwable cause) {
        super(cause);
    }
}

