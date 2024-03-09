package grupo2.pasteurizadora.back_pasteurizadora.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;

import java.time.LocalDate;

@Entity
@Table(name = "HaciendaLechera")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HaciendaLechera {
    @Id
    @NonNull
    @NotNull(message = "El codigo de la hacienda no puede ser nulo")
    private String codHacienda;
    @NotNull(message = "El nombre de la hacienda no puede ser nulo")
    private String nombreHacienda;
    @NotNull(message = "El ruc de la hacienda no puede ser nulo")
    private String ruc;
    @NotNull(message = "La direccion de la hacienda no puede ser nula")
    private String direccion;
    @NotNull(message = "El telefono de la hacienda no puede ser nulo")
    private String telefonoEmpresa;
    @NotNull(message = "El responsable de la hacienda no puede ser nulo")
    private String responsable;
    @NotNull(message = "El email de la hacienda no puede ser nulo")
    @Email(message = "El email debe tener un formato valido")
    private String email;
    @NotNull(message = "El telefono de contacto de la hacienda no puede ser nulo")
    private String telefonoContacto;
    @NotNull(message = "La fecha de compra de la hacienda no puede ser nula")
    private LocalDate fechaCompra;
    @NotNull(message = "Debe especificar el detalle del suministro")
    private String detallesSuministro;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codRecepcion")
    private RecepcionLeche recepcionLeche;

}