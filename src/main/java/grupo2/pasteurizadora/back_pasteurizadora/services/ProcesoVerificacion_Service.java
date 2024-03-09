package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoVerificacionUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoVerificacion_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.LoteProductos;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoPasteurizacion;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoVerificacion;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.LoteProductos_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.ProcesoPasteurizacion_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.ProcesoVerificacion_Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProcesoVerificacion_Service {

    final ProcesoVerificacion_Repository procesoVerificacion_repository;
    final ProcesoPasteurizacion_Repository procesoPasteurizacion_repository;
    final LoteProductos_Repository loteProductos_repository;

    public ProcesoVerificacion_Service(ProcesoVerificacion_Repository procesoVerificacion_repository, ProcesoPasteurizacion_Repository procesoPasteurizacion_repository, LoteProductos_Repository loteProductos_repository) {
        this.procesoVerificacion_repository = procesoVerificacion_repository;
        this.procesoPasteurizacion_repository = procesoPasteurizacion_repository;
        this.loteProductos_repository = loteProductos_repository;
    }

    public ProcesoVerificacion saveProcesoVerificacion(ProcesoVerificacion_dto procesoVerificacion_dto) {
        ProcesoVerificacion procesoVerificacion = new ProcesoVerificacion();
        procesoVerificacion.setCodProcesoVerificacion(procesoVerificacion_dto.getCodProcesoVerificacion());
        procesoVerificacion.setResultadoVerificacion(procesoVerificacion_dto.isResultadoVerificacion());
        if (procesoVerificacion_dto.getProcesoPasteurizacion() != null) {
            ProcesoPasteurizacion procesoPasteurizacion = procesoPasteurizacion_repository.findById(procesoVerificacion_dto.getProcesoPasteurizacion())
                    .orElseThrow(() -> new ClientException("No se encontró ProcesoPasteurizacion con el ID proporcionado"));
            procesoVerificacion.setProcesoPasteurizacion(procesoPasteurizacion);
            procesoPasteurizacion.setProcesoVerificacion(procesoVerificacion);
        }
        procesoVerificacion.setTiempoTratamiento(LocalTime.parse(procesoVerificacion_dto.getTiempoTratamiento()));
        procesoVerificacion.setObservaciones(procesoVerificacion_dto.getObservaciones());
        procesoVerificacion.setDetallesProceso(procesoVerificacion_dto.getDetallesProceso());

        List<LoteProductos> loteProductos = procesoVerificacion_dto.getLoteProductos().stream()
                .map(loteProductos_repository::findById)
                .map(optionalLote -> optionalLote.orElseThrow(() -> new ClientException("No se encontró LoteProductos con el ID proporcionado")))
                .toList();

        for (LoteProductos lote : loteProductos) {
            lote.setProcesoVerificacion(procesoVerificacion);
        }

        procesoVerificacion.setLoteProductos(loteProductos);

        return procesoVerificacion_repository.save(procesoVerificacion);
    }

    public List<ProcesoVerificacion> getAllProcesoVerificacion() {
        return procesoVerificacion_repository.findAll();
    }

    public ProcesoVerificacion getProcesoVerificacionById(String codProcesoVerificacion) {
        return procesoVerificacion_repository.findById(codProcesoVerificacion)
                .orElseThrow(() -> new ClientException("No se encontró ProcesoVerificacion con el ID proporcionado"));
    }

    public void deleteProcesoVerificacion(String codProcesoVerificacion) {
        Optional<ProcesoVerificacion> procesoVerificacionOptional = procesoVerificacion_repository.findById(codProcesoVerificacion);
        if (procesoVerificacionOptional.isPresent()) {
            ProcesoVerificacion procesoVerificacion = procesoVerificacionOptional.get();
            // Primero, desvincula y guarda las entidades LoteProductos
            for (LoteProductos loteProductos : procesoVerificacion.getLoteProductos()) {
                loteProductos.setProcesoVerificacion(null);
                loteProductos_repository.save(loteProductos);
            }
            // Desvincula el ProcesoPasteurizacion
            if (procesoVerificacion.getProcesoPasteurizacion() != null) {
                ProcesoPasteurizacion procesoPasteurizacion = procesoVerificacion.getProcesoPasteurizacion();
                procesoPasteurizacion.setProcesoVerificacion(null);
                procesoPasteurizacion_repository.save(procesoPasteurizacion);
            }
            // Ahora se puede eliminar el ProcesoVerificacion
            procesoVerificacion_repository.deleteById(codProcesoVerificacion);
        } else {
            System.out.println("No se encontró el proceso de verificación");
        }
    }

    public ProcesoVerificacion updateProcesoVerificacion(ProcesoVerificacionUpdateDto procesoVerificacionUpdateDto) {
        // Buscar el ProcesoVerificacion existente
        ProcesoVerificacion procesoVerificacion = procesoVerificacion_repository.findById(procesoVerificacionUpdateDto.getCodProcesoVerificacion())
                .orElseThrow(() -> new ClientException("No se encontró ProcesoVerificacion con el ID proporcionado"));

        // Actualizar los campos del ProcesoVerificacion existente con los valores del DTO
        procesoVerificacion.setResultadoVerificacion(procesoVerificacionUpdateDto.isResultadoVerificacion());
        procesoVerificacion.setTiempoTratamiento(LocalTime.parse(procesoVerificacionUpdateDto.getTiempoTratamiento()));
        procesoVerificacion.setObservaciones(procesoVerificacionUpdateDto.getObservaciones());
        procesoVerificacion.setDetallesProceso(procesoVerificacionUpdateDto.getDetallesProceso());

        // Guardar el ProcesoVerificacion actualizado en la base de datos
        return procesoVerificacion_repository.save(procesoVerificacion);
    }

    //Consultas JPQL
    //Consulta JPQL para traer una lista de los id de los proceso de Pasteurizacion
    public List<String> findCodProcesoPasteurizacion() {
        return procesoVerificacion_repository.findCodProcesoPasteurizacion();
    }

    //Por consultas JPQL traer una lista de los id de los Lotes de productos
    public List<String> findCodLoteProductos() {
        return procesoVerificacion_repository.findCodLoteProductos();
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    public Long countProcesoVerificacion() {
        return procesoVerificacion_repository.countProcesoVerificacion();
    }

}
