package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.entity.HaciendaLechera;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.HaciendaLechera_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HaciendaLechera_Service {

    final HaciendaLechera_Repository haciendaLechera_repository;

    @Autowired
    public HaciendaLechera_Service(HaciendaLechera_Repository haciendaLechera_repository) {
        this.haciendaLechera_repository = haciendaLechera_repository;
    }

    public List<HaciendaLechera> getAll() {
        return haciendaLechera_repository.findAll();
    }

    public HaciendaLechera getHaciendaLecheraByCodHacienda(String codHacienda) {
        Optional<HaciendaLechera> haciendaLechera = haciendaLechera_repository.findById(codHacienda);
        if (haciendaLechera.isPresent()) {
            return haciendaLechera.get();
        } else {
            throw new ClientException("No se encontró la hacienda lechera con codigo: " + codHacienda);
        }
    }

    public HaciendaLechera saveHaciendaLechera(HaciendaLechera haciendaLechera) {
        // Guardar en la base de datos (la operación real se realiza después de publicar en la cola)
        return haciendaLechera_repository.save(haciendaLechera);
    }

    private HaciendaLechera updateHaciendaLecheraFields(HaciendaLechera existingHaciendaLechera, HaciendaLechera newHaciendaLechera) {
        existingHaciendaLechera.setNombreHacienda(newHaciendaLechera.getNombreHacienda());
        existingHaciendaLechera.setRuc(newHaciendaLechera.getRuc());
        existingHaciendaLechera.setDireccion(newHaciendaLechera.getDireccion());
        existingHaciendaLechera.setTelefonoEmpresa(newHaciendaLechera.getTelefonoEmpresa());
        existingHaciendaLechera.setResponsable(newHaciendaLechera.getResponsable());
        existingHaciendaLechera.setEmail(newHaciendaLechera.getEmail());
        existingHaciendaLechera.setTelefonoContacto(newHaciendaLechera.getTelefonoContacto());
        existingHaciendaLechera.setFechaCompra(newHaciendaLechera.getFechaCompra());
        existingHaciendaLechera.setDetallesSuministro(newHaciendaLechera.getDetallesSuministro());
        return existingHaciendaLechera;
    }

    public HaciendaLechera updateHaciendaLechera(HaciendaLechera haciendaLechera) {
        String codHacienda = haciendaLechera.getCodHacienda();
        Optional<HaciendaLechera> optionalHaciendaLechera = haciendaLechera_repository.findById(codHacienda);
        if (optionalHaciendaLechera.isPresent()) {
            HaciendaLechera updatedHaciendaLechera = updateHaciendaLecheraFields(optionalHaciendaLechera.get(), haciendaLechera);

            // Actualizar en la base de datos (la operación real se realiza después de publicar en la cola)
            return haciendaLechera_repository.save(updatedHaciendaLechera);
        } else {
            throw new ClientException("No se encontró la hacienda lechera con codigo: " + codHacienda);
        }
    }

    public void deleteHaciendaLechera(String codHacienda) {
        // Eliminar de la base de datos (la operación real se realiza después de publicar en la cola)
        haciendaLechera_repository.deleteById(codHacienda);
    }

    // Consulta JPQL para saber cuántos datos hay en la tabla
    public Long countHaciendaLechera() {
        return haciendaLechera_repository.countHaciendaLechera();
    }
}
