package grupo2.pasteurizadora.back_pasteurizadora.repository;

import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoPasteurizacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcesoPasteurizacion_Repository extends JpaRepository<ProcesoPasteurizacion, String> {

    //Por consultas JPQL traer una lista de los id de los recepcion de leche
    @Query("SELECT r.codRecepcion FROM RecepcionLeche r")
    List<String> findCodRecepcionLeche();


    //Consulta JPQL para traer una lista de los id de recepcion de leche que no esten en un proceso de pasteurizacion
    @Query("SELECT r.codRecepcion FROM RecepcionLeche r WHERE r.codRecepcion NOT IN (SELECT p.codProcesoPastz FROM ProcesoPasteurizacion p)")
    List<String> findCodRecepcionLecheNotInProcesoPasteurizacion();

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @Query("SELECT COUNT(p) FROM ProcesoPasteurizacion p")
    Long countProcesoPasteurizacion();
}
