package tourGuide.exception.handlers;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class ErrorMessage {
    private int httpStatusCode;
    private Date timestamp;
    private String message;
    private String description;
}
