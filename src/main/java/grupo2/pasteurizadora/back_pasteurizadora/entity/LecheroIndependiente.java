package grupo2.pasteurizadora.back_pasteurizadora.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDate;
@Entity
@Table(name = "LecheroIndependiente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LecheroIndependiente {
    @Id
    @NonNull
    @NotNull(message = "El codigo del lechero no puede ser nulo")
    private String codLechero;
    @NotNull(message = "El nombre del lechero no puede ser nulo")
    private String nombres;
    @NotNull(message = "El apellido del lechero no puede ser nulo")
    private String apellidos;
    @NonNull
    @NotNull(message = "La cedula del lechero no puede ser nula")
    private Integer cedula;
    @NotNull(message = "El telefono del lechero no puede ser nulo")
    private String direccion;
    @NotNull(message = "El telefono del lechero no puede ser nulo")
    private String email;
    @NotNull(message = "El telefono del lechero no puede ser nulo")
    private String contacto;
    @NotNull(message = "Debe especificar la fecha de compra")
    private LocalDate fechaCompra;
    @NotNull(message = "Debe especificar el detalle del suministro")
    private String detallesSuministro;

    @JsonIgnore
    //Un lechero independiente tiene un registro de recepcion de leche
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codRecepcion")
    private RecepcionLeche recepcionLeche;

}