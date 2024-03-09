package grupo2.pasteurizadora.back_pasteurizadora.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ProcesoPasteurizacion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProcesoPasteurizacion {
    @Id
    private String codProcesoPastz;
    private String cantidadLitrosUsados;
    private float temperatura;
    private LocalTime tiempoTratamiento;
    private String tipoProcesamiento;
    private String detallesProceso;

    //Un proceso de pasteurizacion tiene una lista de recepcion de leche
    @OneToMany(mappedBy = "procesoPasteurizacion", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<RecepcionLeche> recepcionLeche;

    //Un proceso de pasteurizacion tiene un proceso de verificacion
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "procesoVerificacion")
    private ProcesoVerificacion procesoVerificacion;


}