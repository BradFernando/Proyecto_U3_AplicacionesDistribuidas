package grupo2.pasteurizadora.back_pasteurizadora.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "LoteProductos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LoteProductos implements Serializable {
    @Id
    private String codLote;
    private String nombreLote;
    private String tipoLote;
    private LocalDate fechadeProduccion;
    private LocalDate fechadeVencimiento;
    private String detallesLote;
    private int cantidadPaquetesTotales;
    private int cantidadPaquetesDisponibles;

    //Muchos productos procesados tienen un proceso de verificacion
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "codProcesoVerificacion")
    private ProcesoVerificacion procesoVerificacion;
}