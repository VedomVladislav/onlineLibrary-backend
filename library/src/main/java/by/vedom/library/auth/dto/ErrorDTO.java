package by.vedom.library.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorDTO {
    private String exception;
    private String message;

    public ErrorDTO(String exception, String message) {
        this.exception = exception;
        this.message = message;
    }
}
