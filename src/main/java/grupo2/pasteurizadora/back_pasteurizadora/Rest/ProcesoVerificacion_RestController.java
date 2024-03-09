package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoVerificacionUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoVerificacion_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoVerificacion;
import grupo2.pasteurizadora.back_pasteurizadora.services.ProcesoVerificacion_Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/procesoVerificacion")
public class ProcesoVerificacion_RestController {

    final
    ProcesoVerificacion_Service procesoVerificacion_service;

    public ProcesoVerificacion_RestController(ProcesoVerificacion_Service procesoVerificacion_service) {
        this.procesoVerificacion_service = procesoVerificacion_service;
    }

    @PostMapping
    public ProcesoVerificacion saveProcesoVerificacion(@RequestBody ProcesoVerificacion_dto procesoVerificacion_dto){
        return procesoVerificacion_service.saveProcesoVerificacion(procesoVerificacion_dto);
    }

    @GetMapping
    public List<ProcesoVerificacion> getAllProcesoVerificacion(){
        return procesoVerificacion_service.getAllProcesoVerificacion();
    }

    @GetMapping("/{codProcesoVerificacion}")
    public ProcesoVerificacion getProcesoVerificacionById(@PathVariable String codProcesoVerificacion){
        return procesoVerificacion_service.getProcesoVerificacionById(codProcesoVerificacion);
    }

    @DeleteMapping("/{codProcesoVerificacion}")
    public void deleteProcesoVerificacion(@PathVariable String codProcesoVerificacion){
        procesoVerificacion_service.deleteProcesoVerificacion(codProcesoVerificacion);
    }

    @PutMapping
    public ProcesoVerificacion updateProcesoVerificacion(@RequestBody ProcesoVerificacionUpdateDto procesoVerificacionUpdateDto){
        return procesoVerificacion_service.updateProcesoVerificacion(procesoVerificacionUpdateDto);
    }

    //Consultas JPQL

    @GetMapping("/ProcesosPasteurizacion_cod")
    public List<String> getCodRecepcionLeche(){
        return procesoVerificacion_service.findCodProcesoPasteurizacion();
    }
    //Por consultas JPQL traer una lista de los id de los Lotes de productos
    @GetMapping("/LoteProductos_cod")
    public List<String> getCodLoteProductos(){
        return procesoVerificacion_service.findCodLoteProductos();
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countProcesoVerificacion() {
        return procesoVerificacion_service.countProcesoVerificacion();
    }

}