package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoPasteurizacionUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.ProcesoPasteurizacion_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.ProcesoPasteurizacion;
import grupo2.pasteurizadora.back_pasteurizadora.services.ProcesoPasteurizacion_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/procesoPasteurizacion")
public class ProcesoPasteurizacion_RestController {

    @Autowired
    ProcesoPasteurizacion_Service procesoPasteurizacion_service;

    @GetMapping
    public List<ProcesoPasteurizacion> getAll(){
        return procesoPasteurizacion_service.getAllProcesoPasteurizacion();
    }

    @PostMapping
    public ProcesoPasteurizacion saveProcesoPasteurizacion(@RequestBody ProcesoPasteurizacion_dto procesoPasteurizacion_dto){
        return procesoPasteurizacion_service.saveProcesoPasteurizacion(procesoPasteurizacion_dto);
    }

    @GetMapping("/{codProcesoPastz}")
    public ProcesoPasteurizacion getProcesoPasteurizacionById(@PathVariable String codProcesoPastz){
        return procesoPasteurizacion_service.getProcesoPasteurizacionById(codProcesoPastz);
    }

    @PutMapping
    public ProcesoPasteurizacion updateProcesoPasteurizacion(@RequestBody ProcesoPasteurizacionUpdateDto procesoPasteurizacionUpdateDto){
        return procesoPasteurizacion_service.updateProcesoPasteurizacion(procesoPasteurizacionUpdateDto);
    }

    @DeleteMapping("/{codProcesoPastz}")
    public void deleteProcesoPasteurizacion(@PathVariable String codProcesoPastz){
        procesoPasteurizacion_service.deleteProcesoPasteurizacion(codProcesoPastz);
    }

    //Consultas JPQL
    @GetMapping("/RecepcionLeche_cod")
    public List<String> getCodRecepcionLeche(){
        return procesoPasteurizacion_service.findCodRecepcion();
    }

    @GetMapping("/RecepcionLeche_cod_not_in")
    public List<String> getCodRecepcionLecheNotInProcesoPasteurizacion(){
        return procesoPasteurizacion_service.findCodRecepcionNotInProcesoPasteurizacion();
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countProcesoPasteurizacion() {
        return procesoPasteurizacion_service.countProcesoPasteurizacion();
    }

}
