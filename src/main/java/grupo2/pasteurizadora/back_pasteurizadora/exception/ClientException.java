package grupo2.pasteurizadora.back_pasteurizadora.exception;

import grupo2.pasteurizadora.back_pasteurizadora.dto.ErrorResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ClientException extends RuntimeException{

    private final ErrorResponse errorResponse;
    private HttpStatus httpStatusCode = HttpStatus.BAD_REQUEST;


    public ClientException(String message, HttpStatus httpStatusCode) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorResponse = new ErrorResponse(message, httpStatusCode.value());
    }

    public ClientException(String message) {
        super(message);
        this.errorResponse = new ErrorResponse(message, httpStatusCode.value());
    }

    public ClientException(String message, String target) {
        super(message);
        this.errorResponse = new ErrorResponse(message, httpStatusCode.value(), target);
    }
    public ClientException(String message, HttpStatus httpStatusCode, String target) {
        super(message);
        this.httpStatusCode = httpStatusCode;
        this.errorResponse = new  ErrorResponse(message, httpStatusCode.value(), target);
    }

}
