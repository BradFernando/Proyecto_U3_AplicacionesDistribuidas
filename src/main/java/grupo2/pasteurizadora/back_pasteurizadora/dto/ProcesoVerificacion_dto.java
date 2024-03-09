package grupo2.pasteurizadora.back_pasteurizadora.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class ProcesoVerificacion_dto {
    @Id
    @NonNull
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
    @NotNull(message = "Debe ingresar el codigo del proceso de pasteurizacion")
    private String procesoPasteurizacion;
    @NotNull(message = "Debe ingresar los codigo del lote de productos")
    private List<String> loteProductos;


    public ProcesoVerificacion_dto() {}

}
