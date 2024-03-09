package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.config.RabbitMQConfig;
import grupo2.pasteurizadora.back_pasteurizadora.dto.LoteCantidadDTO;
import grupo2.pasteurizadora.back_pasteurizadora.dto.LoteResponseDTO;
import grupo2.pasteurizadora.back_pasteurizadora.entity.LoteProductos;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoVerificacion;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.LoteProductos_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.ProcesoVerificacion_Repository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoteProductos_Service {

    final LoteProductos_Repository loteProductos_repository;
    final ProcesoVerificacion_Repository procesoVerificacion_repository;
    final RabbitTemplate rabbitTemplate;

    @Autowired
    public LoteProductos_Service(LoteProductos_Repository loteProductos_repository, ProcesoVerificacion_Repository procesoVerificacion_repository, RabbitTemplate rabbitTemplate) {
        this.loteProductos_repository = loteProductos_repository;
        this.procesoVerificacion_repository = procesoVerificacion_repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public List<LoteProductos> getAllLoteProductos() {
        return loteProductos_repository.findAll();
    }

    public LoteProductos saveLoteProductos(LoteProductos loteProductos) {
        return loteProductos_repository.save(loteProductos);
    }

    public LoteProductos getLoteProductosbyCodLote(String codLote) {
        return loteProductos_repository.findById(codLote)
                .orElseThrow(() -> new ClientException("No se encuentra el codigo de Lote Proporcionado: " + codLote));
    }

    public LoteProductos updateLoteProductos(LoteProductos loteProductos) {
        LoteProductos existingLoteProductos = getLoteProductosbyCodLote(loteProductos.getCodLote());
        existingLoteProductos.setNombreLote(loteProductos.getNombreLote());
        existingLoteProductos.setTipoLote(loteProductos.getTipoLote());
        existingLoteProductos.setFechadeProduccion(loteProductos.getFechadeProduccion());
        existingLoteProductos.setFechadeVencimiento(loteProductos.getFechadeVencimiento());
        existingLoteProductos.setDetallesLote(loteProductos.getDetallesLote());
        existingLoteProductos.setCantidadPaquetesTotales(loteProductos.getCantidadPaquetesTotales());
        existingLoteProductos.setCantidadPaquetesDisponibles(loteProductos.getCantidadPaquetesDisponibles());
        return loteProductos_repository.save(existingLoteProductos);
    }

    public void deleteLoteProductos(String codLote) {
        Optional<LoteProductos> loteProductosOptional = loteProductos_repository.findById(codLote);
        if (loteProductosOptional.isPresent()) {
            LoteProductos loteProductos = loteProductosOptional.get();

            // Desvincula el ProcesoVerificacion
            if (loteProductos.getProcesoVerificacion() != null) {
                ProcesoVerificacion procesoVerificacion = loteProductos.getProcesoVerificacion();
                procesoVerificacion.getLoteProductos().remove(loteProductos);
                procesoVerificacion_repository.save(procesoVerificacion);
            }
            loteProductos_repository.delete(loteProductos);
        } else {
            throw new ClientException("No se encuentra el codigo de Lote Proporcionado: " + codLote);
        }
    }

    public LoteProductos updateCantidadPaquetesDisponibles(String codLote, int cantidad) {
        LoteProductos loteProductos = getLoteProductosbyCodLote(codLote);
        int nuevaCantidad = loteProductos.getCantidadPaquetesDisponibles() - cantidad;

        if (nuevaCantidad < 0) {
            throw new ClientException("La cantidad ingresada es mayor que la cantidad de paquetes disponibles");
        }
        loteProductos.setCantidadPaquetesDisponibles(nuevaCantidad);

        return loteProductos_repository.save(loteProductos);
    }

    public Long countLoteProductos() {
        return loteProductos_repository.countLoteProductos();
    }

    public Long countLoteProductosByYear(Integer year) {
        return loteProductos_repository.countLoteProductosByYear(year);
    }

    public List<Object[]> listCodLoteNombreLote() {
        return loteProductos_repository.listCodLoteNombreLote();
    }

    public Integer getCantidadPaquetesDisponiblesByCodLote(String codLote) {
        return loteProductos_repository.getCantidadPaquetesDisponiblesByCodLote(codLote);
    }

    public List<LoteResponseDTO> enviarProductos(List<LoteCantidadDTO> lotesCantidad) {
        List<LoteResponseDTO> respuesta = new ArrayList<>();
        for (LoteCantidadDTO loteCantidad : lotesCantidad) {
            LoteProductos loteProductos = getLoteProductosbyCodLote(loteCantidad.getId_lote());
            LoteResponseDTO loteResponse = new LoteResponseDTO();
            loteResponse.setId_lote(loteProductos.getCodLote());
            loteResponse.setNombre(loteProductos.getNombreLote());
            loteResponse.setCantidadPaquetesDisponibles(loteCantidad.getCantidadPaquetesDisponibles());
            respuesta.add(loteResponse);
        }
        return respuesta;
    }
}
