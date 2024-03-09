package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.config.RabbitMQConfig;
import grupo2.pasteurizadora.back_pasteurizadora.entity.LecheroIndependiente;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.LecheroIndependiente_Repository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LecheroIndependiente_Service {

    final LecheroIndependiente_Repository lecheroIndependiente_repository;

    @Autowired
    public LecheroIndependiente_Service(LecheroIndependiente_Repository lecheroIndependiente_repository) {
        this.lecheroIndependiente_repository = lecheroIndependiente_repository;
    }

    public List<LecheroIndependiente> getAll() {
        return lecheroIndependiente_repository.findAll();
    }

    public LecheroIndependiente getLecheroIndependienteByCodLechero(String codLechero) {
        Optional<LecheroIndependiente> lecheroIndependiente = lecheroIndependiente_repository.findById(codLechero);
        if (lecheroIndependiente.isPresent()) {
            return lecheroIndependiente.get();
        } else {
            throw new ClientException("No se encontró el lechero independiente con codigo: " + codLechero);
        }
    }

    public LecheroIndependiente saveLecheroIndependiente(LecheroIndependiente lecheroIndependiente) {
        // Guardar en la base de datos (la operación real se realiza después de publicar en la cola)
        return lecheroIndependiente_repository.save(lecheroIndependiente);
    }

    private LecheroIndependiente updateLecheroIndependienteFields(LecheroIndependiente existingLecheroIndependiente, LecheroIndependiente newLecheroIndependiente) {
        existingLecheroIndependiente.setNombres(newLecheroIndependiente.getNombres());
        existingLecheroIndependiente.setApellidos(newLecheroIndependiente.getApellidos());
        existingLecheroIndependiente.setCedula(newLecheroIndependiente.getCedula());
        existingLecheroIndependiente.setDireccion(newLecheroIndependiente.getDireccion());
        existingLecheroIndependiente.setEmail(newLecheroIndependiente.getEmail());
        existingLecheroIndependiente.setContacto(newLecheroIndependiente.getContacto());
        existingLecheroIndependiente.setFechaCompra(newLecheroIndependiente.getFechaCompra());
        existingLecheroIndependiente.setDetallesSuministro(newLecheroIndependiente.getDetallesSuministro());
        return existingLecheroIndependiente;
    }

    public LecheroIndependiente updateLecheroIndependiente(LecheroIndependiente lecheroIndependiente) {
        String codLechero = lecheroIndependiente.getCodLechero();
        Optional<LecheroIndependiente> optionalLecheroIndependiente = lecheroIndependiente_repository.findById(codLechero);
        if (optionalLecheroIndependiente.isPresent()) {
            LecheroIndependiente updatedLecheroIndependiente = updateLecheroIndependienteFields(optionalLecheroIndependiente.get(), lecheroIndependiente);

            // Actualizar en la base de datos (la operación real se realiza después de publicar en la cola)
            return lecheroIndependiente_repository.save(updatedLecheroIndependiente);
        } else {
            throw new ClientException("No se encontró el lechero independiente con codigo: " + codLechero);
        }
    }

    public void deleteLecheroIndependiente(String codLechero) {
        // Eliminar de la base de datos (la operación real se realiza después de publicar en la cola)
        lecheroIndependiente_repository.deleteById(codLechero);
    }

    // Consulta JPQL para saber cuántos datos hay en la tabla
    public Long countLecheroIndependiente() {
        return lecheroIndependiente_repository.countLecheroIndependiente();
    }
}
