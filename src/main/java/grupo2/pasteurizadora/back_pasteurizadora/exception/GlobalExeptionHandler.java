package grupo2.pasteurizadora.back_pasteurizadora.exception;


import grupo2.pasteurizadora.back_pasteurizadora.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientException e){
        return ResponseEntity.status(e.getHttpStatusCode()).body(e.getErrorResponse());
    }

}
