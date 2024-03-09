package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.config.RabbitMQConfig;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoPasteurizacionUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoPasteurizacion_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoPasteurizacion;
import grupo2.pasteurizadora.back_pasteurizadora.entity.RecepcionLeche;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.ProcesoPasteurizacion_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.RecepcionLeche_Repository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcesoPasteurizacion_Service {

    final ProcesoPasteurizacion_Repository procesoPasteurizacion_repository;
    final RecepcionLeche_Repository recepcionLeche_repository;

    @Autowired
    public ProcesoPasteurizacion_Service(ProcesoPasteurizacion_Repository procesoPasteurizacion_repository, RecepcionLeche_Repository recepcionLeche_repository) {
        this.procesoPasteurizacion_repository = procesoPasteurizacion_repository;
        this.recepcionLeche_repository = recepcionLeche_repository;
    }

    public ProcesoPasteurizacion saveProcesoPasteurizacion(ProcesoPasteurizacion_dto procesoPasteurizacion_dto) {
        ProcesoPasteurizacion procesoPasteurizacion = new ProcesoPasteurizacion();
        procesoPasteurizacion.setCodProcesoPastz(procesoPasteurizacion_dto.getCodProcesoPastz());
        procesoPasteurizacion.setCantidadLitrosUsados(procesoPasteurizacion_dto.getCantidadLitrosUsados());
        procesoPasteurizacion.setTemperatura(procesoPasteurizacion_dto.getTemperatura());
        procesoPasteurizacion.setTiempoTratamiento(procesoPasteurizacion_dto.getTiempoTratamiento());
        procesoPasteurizacion.setTipoProcesamiento(procesoPasteurizacion_dto.getTipoProcesamiento());
        procesoPasteurizacion.setDetallesProceso(procesoPasteurizacion_dto.getDetallesProceso());

        List<RecepcionLeche> recepcionLeche = procesoPasteurizacion_dto.getRecepcionLeche().stream()
                .map(recepcionLeche_repository::findById)
                .map(optionalRecepcion -> optionalRecepcion.orElseThrow(() -> new ClientException("No se encontró RecepcionLeche con el ID proporcionado")))
                .toList();

        for (RecepcionLeche recepcion : recepcionLeche) {
            recepcion.setProcesoPasteurizacion(procesoPasteurizacion);
        }

        procesoPasteurizacion.setRecepcionLeche(recepcionLeche);

        // Guardar en la base de datos (la operación real se realiza después de publicar en la cola)
        return procesoPasteurizacion_repository.save(procesoPasteurizacion);
    }

    public List<ProcesoPasteurizacion> getAllProcesoPasteurizacion() {
        return procesoPasteurizacion_repository.findAll();
    }

    public ProcesoPasteurizacion getProcesoPasteurizacionById(String codProcesoPastz) {
        return procesoPasteurizacion_repository.findById(codProcesoPastz)
                .orElseThrow(() -> new ClientException("No se encontró ProcesoPasteurizacion con el ID proporcionado"));
    }

    public ProcesoPasteurizacion updateProcesoPasteurizacion(ProcesoPasteurizacionUpdateDto procesoPasteurizacionDto) {
        Optional<ProcesoPasteurizacion> existingProcesoPasteurizacionOptional = procesoPasteurizacion_repository.findById(procesoPasteurizacionDto.getCodProcesoPastz());
        if (existingProcesoPasteurizacionOptional.isEmpty()) {
            throw new ClientException("No se encontró ProcesoPasteurizacion con el CodProcesoPastz proporcionado" + procesoPasteurizacionDto.getCodProcesoPastz());
        }
        ProcesoPasteurizacion existingProcesoPasteurizacion = getExistingProcesoPasteurizacion(procesoPasteurizacionDto.getCodProcesoPastz());
        existingProcesoPasteurizacion.setCantidadLitrosUsados(procesoPasteurizacionDto.getCantidadLitrosUsados());
        existingProcesoPasteurizacion.setTemperatura(procesoPasteurizacionDto.getTemperatura());
        existingProcesoPasteurizacion.setTiempoTratamiento(procesoPasteurizacionDto.getTiempoTratamiento());
        existingProcesoPasteurizacion.setTipoProcesamiento(procesoPasteurizacionDto.getTipoProcesamiento());
        existingProcesoPasteurizacion.setDetallesProceso(procesoPasteurizacionDto.getDetallesProceso());

        // Guardar en la base de datos (la operación real se realiza después de publicar en la cola)
        return procesoPasteurizacion_repository.save(existingProcesoPasteurizacion);
    }

    private ProcesoPasteurizacion getExistingProcesoPasteurizacion(String codProcesoPastz) {
        Optional<ProcesoPasteurizacion> existingProcesoPasteurizacionOptional = procesoPasteurizacion_repository.findById(codProcesoPastz);
        if (existingProcesoPasteurizacionOptional.isEmpty()) {
            throw new ClientException("No se encontró ProcesoPasteurizacion con el CodProcesoPastz proporcionado" + codProcesoPastz);
        }
        return existingProcesoPasteurizacionOptional.get();
    }

    public void deleteProcesoPasteurizacion(String codProcesoPastz) {
        Optional<ProcesoPasteurizacion> ProcesoPasteurizacionOptional = procesoPasteurizacion_repository.findById(codProcesoPastz);
        if (ProcesoPasteurizacionOptional.isPresent()) {
            ProcesoPasteurizacion procesoPasteurizacion = ProcesoPasteurizacionOptional.get();
            // Primero, desvincula y guarda las entidades RecepcionLeche
            for (RecepcionLeche recepcion : procesoPasteurizacion.getRecepcionLeche()) {
                recepcion.setProcesoPasteurizacion(null);
                recepcionLeche_repository.save(recepcion);
            }
            // Eliminar de la base de datos (la operación real se realiza después de publicar en la cola)
            procesoPasteurizacion_repository.deleteById(codProcesoPastz);
        } else {
            System.out.println("No se encontró el proceso de pasteurización");
        }
    }

    // Consultas JPQL
    public List<String> findCodRecepcion() {
        return procesoPasteurizacion_repository.findCodRecepcionLeche();
    }

    public List<String> findCodRecepcionNotInProcesoPasteurizacion() {
        return procesoPasteurizacion_repository.findCodRecepcionLecheNotInProcesoPasteurizacion();
    }

    // Consulta JPQL para saber cuántos datos hay en la tabla
    public Long countProcesoPasteurizacion() {
        return procesoPasteurizacion_repository.countProcesoPasteurizacion();
    }
}
