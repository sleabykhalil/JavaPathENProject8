package tourGuide.exception.handlers;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ErrorMessage {
    private int httpStatusCode;
    private LocalDateTime timestamp;
    private String message;
    private String description;
}
