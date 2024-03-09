package grupo2.pasteurizadora.back_pasteurizadora.repository;

import grupo2.pasteurizadora.back_pasteurizadora.entity.LoteProductos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoteProductos_Repository extends JpaRepository<LoteProductos, String> {

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @Query("SELECT COUNT(l) FROM LoteProductos l")
    Long countLoteProductos();

    //Cantidad lotes por año
    @Query("SELECT COUNT(l) FROM LoteProductos l WHERE EXTRACT(YEAR FROM l.fechadeProduccion) = ?1")
    Long countLoteProductosByYear(Integer year);

    //Listar todos los codLote, nombreLote de la tabla LoteProductos cuya cantidadPaquetesDisponibles sea mayor a 0
    @Query("SELECT l.codLote, l.nombreLote FROM LoteProductos l WHERE l.cantidadPaquetesDisponibles > 0")
    List<Object[]> listCodLoteNombreLote();

    //Obtener la cantidadPaquetesDisponibles de un lote por su codLote
    @Query("SELECT l.cantidadPaquetesDisponibles FROM LoteProductos l WHERE l.codLote = ?1")
    Integer getCantidadPaquetesDisponiblesByCodLote(String codLote);

    //Obtener una lista de nombreLote de un lote por su codLote
    @Query("SELECT l.nombreLote FROM LoteProductos l WHERE l.codLote = ?1")
    List<Object[]> getNombreLoteByCodLote(String codLote);



}
