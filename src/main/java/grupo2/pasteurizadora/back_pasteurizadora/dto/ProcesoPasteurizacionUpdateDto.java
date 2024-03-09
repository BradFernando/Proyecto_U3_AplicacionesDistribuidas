package grupo2.pasteurizadora.back_pasteurizadora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalTime;
import java.util.List;

@Data
public class ProcesoPasteurizacionUpdateDto {
    @Id
    private String codProcesoPastz;
    private String cantidadLitrosUsados;
    private float temperatura;
    private LocalTime tiempoTratamiento;
    private String tipoProcesamiento;
    private String detallesProceso;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> recepcionLeche;

}
