package grupo2.pasteurizadora.back_pasteurizadora.dto;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;
import java.util.List;

@Data
public class RecepcionHaciendasLecheras_dto {
    @Id
    @NonNull
    @NotNull(message = "Se debe ingresar un codigo de recepcion")
    private String codRecepcion;
    @NotNull(message = "Debe ingresar la fecha de recepcion")
    private LocalDate fechaRecepcion;
    @NotNull(message = "Debe ingresar los resultados de las pruebas de calidad")
    private String resultadosPruebasCalidad;
    @NotNull(message = "Debe ingresar la cantidad de leche recibida")
    private int cantidadLecheRecibida;
    @NotNull(message = "Debe ingresar el pago total")
    private String pagoTotal;
    @NotNull(message = "Debe ingresar el codigo de lechero")
    private List<String> haciendaLechera;

    public RecepcionHaciendasLecheras_dto() {}

}
