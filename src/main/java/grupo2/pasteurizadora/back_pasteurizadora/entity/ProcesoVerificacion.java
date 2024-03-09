package grupo2.pasteurizadora.back_pasteurizadora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "ProcesoVerificacion")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProcesoVerificacion {
    @Id
    private String codProcesoVerificacion;
    private boolean resultadoVerificacion;
    private LocalTime tiempoTratamiento;
    private String observaciones;
    private String detallesProceso;

    //Un proceso de verificacion tiene un proceso de pasteurizacion
    @OneToOne(mappedBy = "procesoVerificacion", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private ProcesoPasteurizacion procesoPasteurizacion;

    //Un proceso de verificacion tiene muchos productos procesados
    @OneToMany(mappedBy = "procesoVerificacion", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<LoteProductos> loteProductos;
}