package grupo2.pasteurizadora.back_pasteurizadora.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "RecepcionLeche")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RecepcionLeche {
    @Id
    private String codRecepcion;
    private LocalDate fechaRecepcion;
    private String resultadosPruebasCalidad;
    private int cantidadLecheRecibida;
    private String pagoTotal;


    //Una recepcion de leche tiene una lista de codHacienda
    @OneToMany(mappedBy = "recepcionLeche", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<HaciendaLechera> haciendaLechera;

    //Una recepcion de leche tiene una lista de codLechero
    @OneToMany(mappedBy = "recepcionLeche", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<LecheroIndependiente> lecheroIndependiente;

    //Muchas recepciones de leche tienen un proceso de pasteurizacion
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codProcesoPastz")
    private ProcesoPasteurizacion procesoPasteurizacion;
}