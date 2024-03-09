package grupo2.pasteurizadora.back_pasteurizadora.repository;

import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoVerificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProcesoVerificacion_Repository  extends JpaRepository<ProcesoVerificacion, String>{

    //Por consultas JPQL traer una lista de los id de los procesos de pasteurizacion
    @Query("SELECT p.codProcesoPastz FROM ProcesoPasteurizacion p")
    List<String> findCodProcesoPasteurizacion();

    //Por consultas JPQL traer una lista de los id de los Lotes de productos
    @Query("SELECT l.codLote FROM LoteProductos l")
    List<String> findCodLoteProductos();

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @Query("SELECT COUNT(p) FROM ProcesoVerificacion p")
    Long countProcesoVerificacion();

}
