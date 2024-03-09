package grupo2.pasteurizadora.back_pasteurizadora.exception;


import grupo2.pasteurizadora.back_pasteurizadora.dto.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Log4j2
@ControllerAdvice
public class GlobalExeptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientException e) {
        return ResponseEntity.status(e.getHttpStatusCode()).body(e.getErrorResponse());
    }

    private static final Function<ConstraintViolation<?>, ErrorResponse> constraintViolationToER = constraintViolation -> {
        var path = constraintViolation.getPropertyPath().toString().split("\\.");
        var param = path[path.length - 1];
        return new ErrorResponse(constraintViolation.getMessage(), null, param);
    };

    private static final Function<ObjectError, ErrorResponse> objectErrorErrorToER = error -> {
        if (error instanceof FieldError errField) {
            return new ErrorResponse(errField.getDefaultMessage(), null, errField.getField());
        } else {
            return new ErrorResponse(error.getDefaultMessage());
        }
    };

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.warn("Se recibió una excepción de AuthenticationException: {}", ex.getMessage());
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<List<ErrorResponse>> handleConstraintViolationException(ConstraintViolationException ex) {
        var constraintViolations = ex.getConstraintViolations();
        var errors = constraintViolations
                .stream()
                .map(constraintViolationToER)
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMessage = "Error: El parámetro requerido '" + ex.getParameterName() + "' no está presente en la solicitud.";
        return ResponseEntity.badRequest().body(new ErrorResponse(errorMessage, 400, ex.getParameterName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErrorResponse>> handleValidationException(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = ex
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(objectErrorErrorToER)
                .toList();

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex) {
        log.warn("Se recibió una excepción de SQLIntegrityConstraintViolationException, se recomienda implementar el método onSQLIntegrityConstraintViolationException de la clase   GlobalExcepcionHandler para evitar fugas de información del esquema de base de datos: {} ", ex.getMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse("Error de integridad de datos"));
    }


}
