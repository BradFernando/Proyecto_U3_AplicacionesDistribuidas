package grupo2.pasteurizadora.back_pasteurizadora.dto;

import lombok.Data;

@Data
public class LoteResponseDTO {
    private String id_lote;
    private String nombre;
    private int cantidadPaquetesDisponibles;
}