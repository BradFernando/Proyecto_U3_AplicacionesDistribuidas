package grupo2.pasteurizadora.back_pasteurizadora.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalTime;
import java.util.List;

@Data
@NoArgsConstructor
public class ProcesoPasteurizacion_dto {
    @Id
    @NonNull
    @NotNull(message = "El codigo del proceso de pasteurizacion no puede ser nulo")
    private String codProcesoPastz;
    @NotNull(message = "La cantidad de litros usados en el proceso de pasteurizacion no puede ser nula")
    private String cantidadLitrosUsados;
    @NotNull(message = "Debe ingresar la temperatura empleada en el proceso de pasteurizacion")
    private float temperatura;
    @NotNull(message = "Debe ingresar el tiempo de tratamiento")
    private LocalTime tiempoTratamiento;
    @NotNull(message = "Debe ingresar el tipo de procesamiento")
    private String tipoProcesamiento;
    @NotNull(message = "Debe ingresar el detalle del proceso")
    private String detallesProceso;


    private List<String> recepcionLeche;

}
