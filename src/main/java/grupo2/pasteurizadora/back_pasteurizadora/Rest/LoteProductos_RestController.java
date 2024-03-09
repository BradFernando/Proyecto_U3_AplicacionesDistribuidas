package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.dto.LoteCantidadDTO;
import grupo2.pasteurizadora.back_pasteurizadora.dto.LoteResponseDTO;
import grupo2.pasteurizadora.back_pasteurizadora.entity.LoteProductos;
import grupo2.pasteurizadora.back_pasteurizadora.services.LoteProductos_Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/loteProductos")
public class LoteProductos_RestController {

    final
    LoteProductos_Service loteProductos_service;

    public LoteProductos_RestController(LoteProductos_Service loteProductos_service) {
        this.loteProductos_service = loteProductos_service;
    }

    @GetMapping
    public List<LoteProductos> getLoteProductos() {
        return loteProductos_service.getAllLoteProductos();
    }

    @PostMapping
    public LoteProductos saveLoteProductos(@RequestBody LoteProductos loteProductos) {
        return loteProductos_service.saveLoteProductos(loteProductos);
    }

    @GetMapping("/{codLote}")
    public LoteProductos getLoteProductosbyCodLote(@PathVariable String codLote) {
        return loteProductos_service.getLoteProductosbyCodLote(codLote);
    }

    @PutMapping
    public LoteProductos updateLoteProductos(@RequestBody LoteProductos loteProductos) {
        return loteProductos_service.updateLoteProductos(loteProductos);
    }

    @DeleteMapping("/{codLote}")
    public void deleteLoteProductos(@PathVariable String codLote) {
        loteProductos_service.deleteLoteProductos(codLote);
    }

    //Actualiza la cantidadPaquetesDisponibles de un lote por su codLote
    @PutMapping("/updateCantidadPaquetesDisponibles/{codLote}/{cantidadPaquetesDisponibles}")
    public LoteProductos updateCantidadPaquetesDisponibles(@PathVariable String codLote, @PathVariable Integer cantidadPaquetesDisponibles) {
        return loteProductos_service.updateCantidadPaquetesDisponibles(codLote, cantidadPaquetesDisponibles);
    }

    //Consultas JPQL
    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countLoteProductos() {
        return loteProductos_service.countLoteProductos();
    }

    //Cantidad lotes por año
    @GetMapping("/countByYear/{year}")
    public Long countLoteProductosByYear(@PathVariable Integer year) {
        return loteProductos_service.countLoteProductosByYear(year);
    }

    //Listar todos los codLote, nombreLote de la tabla LoteProductos cuya cantidadPaquetesDisponibles sea mayor a 0
    @GetMapping("/listCodLoteNombreLote")
    public List<Object[]> listCodLoteNombreLote() {
        return loteProductos_service.listCodLoteNombreLote();
    }

    //Obtener la cantidadPaquetesDisponibles de un lote por su codLote
    @GetMapping("/getCantidadPaquetesDisponiblesByCodLote/{codLote}")
    public Integer getCantidadPaquetesDisponiblesByCodLote(@PathVariable String codLote) {
        return loteProductos_service.getCantidadPaquetesDisponiblesByCodLote(codLote);
    }

    @PostMapping("/enviarProductos")
    public List<LoteResponseDTO> enviarProductos(@RequestBody List<LoteCantidadDTO> lotesCantidad) {
        return loteProductos_service.enviarProductos(lotesCantidad);
    }
}
