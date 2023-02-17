package shop.mtcoding.bankapp.handler.ex;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private HttpStatus status;

    public CustomException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }

    public CustomException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
