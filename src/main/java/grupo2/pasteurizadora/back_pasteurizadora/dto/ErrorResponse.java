package grupo2.pasteurizadora.back_pasteurizadora.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;


@Data
@NoArgsConstructor
public class ErrorResponse {
    private String message;
    private Integer status;
    private Object target;

    public ErrorResponse(String message) {
        this.message = message;
        this.status = 400;
    }

    public ErrorResponse(String message, Integer status) {
        this.message = message;
        this.status = status;
    }

    public ErrorResponse(String message, Integer status, Object target) {
        this.message = message;
        this.status = status;
        this.target = target;
    }

}
