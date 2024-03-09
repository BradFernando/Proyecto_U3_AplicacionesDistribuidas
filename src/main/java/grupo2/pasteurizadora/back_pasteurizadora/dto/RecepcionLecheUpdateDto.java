package grupo2.pasteurizadora.back_pasteurizadora.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RecepcionLecheUpdateDto {
    @Id
    private String codRecepcion;
    private LocalDate fechaRecepcion;
    private String resultadosPruebasCalidad;
    private int cantidadLecheRecibida;
    private String pagoTotal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> haciendaLechera;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> lecheroIndependiente;
}