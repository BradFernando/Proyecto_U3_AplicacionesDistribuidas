package grupo2.pasteurizadora.back_pasteurizadora.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProcesoVerificacionUpdateDto {
    @Id
    @NotNull(message = "Se debe ingresar un codigo de proceso de verificacion")
    private String codProcesoVerificacion;
    @NotNull(message = "Debe ingresar el resultado de la verificacion")
    private boolean resultadoVerificacion;
    @NotNull(message = "Debe ingresar el tiempo de tratamiento")
    private String tiempoTratamiento;
    @NotNull(message = "Debe ingresar las observaciones")
    private String observaciones;
    @NotNull(message = "Debe ingresar los detalles del proceso")
    private String detallesProceso;

    @JsonProperty(access = Access.READ_ONLY)
    private String procesoPasteurizacion;

    @JsonProperty(access = Access.READ_ONLY)
    private List<String> loteProductos;
}