package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepcionHaciendasLecheras_dto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepcionLecheUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepciondeLechero_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.RecepcionLeche;
import grupo2.pasteurizadora.back_pasteurizadora.services.RecepcionLeche_Service;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/recepcionLeche")
public class RecepcionLeche_RestController {

    final
    RecepcionLeche_Service recepcionLeche_service;

    public RecepcionLeche_RestController(RecepcionLeche_Service recepcionLeche_service) {
        this.recepcionLeche_service = recepcionLeche_service;
    }

    @GetMapping
    public List<RecepcionLeche> getAll() {
        return recepcionLeche_service.getAllRecepcionLeche();
    }

    @GetMapping("/{codRecepcion}")
    public RecepcionLeche getRecepcionLecheByCodRecepcion(@PathVariable String codRecepcion) {
        return recepcionLeche_service.getRecepcionLecheById(codRecepcion);
    }

    @PostMapping("/lecheroIndependiente")
    public RecepcionLeche saveRecepcionLecheroIndependiente(@RequestBody RecepciondeLechero_dto recepciondeLechero_dto) {
        return recepcionLeche_service.saveRecepcionLecheroIndependiente(recepciondeLechero_dto);
    }

    @PostMapping("/haciendaLechera")
    public RecepcionLeche saveRecepcionHaciendaLechera(@RequestBody RecepcionHaciendasLecheras_dto recepcionHaciendasLecheras_dto){
        return recepcionLeche_service.saveRecepcionHaciendaLechera(recepcionHaciendasLecheras_dto);
    }

    @PutMapping
    public RecepcionLeche updateRecepcionLeche(@RequestBody RecepcionLecheUpdateDto recepcionLecheUpdateDto){
        return recepcionLeche_service.updateRecepcionLeche(recepcionLecheUpdateDto);
    }

    @DeleteMapping("/{codRecepcion}")
    public void deleteRecepcionLeche(@PathVariable String codRecepcion){
        recepcionLeche_service.deleteRecepcionLeche(codRecepcion);
    }

    //Consultas JPQL
    @GetMapping("/lecheroIndependiente_cod")
    public List<String> getCodLechero(){
        return recepcionLeche_service.findCodLechero();
    }

    @GetMapping("/haciendaLechera_cod")
    public List<String> getCodHacienda(){
        return recepcionLeche_service.findCodHacienda();
    }

    @GetMapping("/lecheroIndependiente_cod_sinRecepcion")
    public List<String> getCodLecheroSinRecepcion(){
        return recepcionLeche_service.findCodLecheroSinRecepcion();
    }

    @GetMapping("/haciendaLechera_cod_sinRecepcion")
    public List<String> getCodHaciendaSinRecepcion(){
        return recepcionLeche_service.findCodHaciendaSinRecepcion();
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countRecepcionLeche() {
        return recepcionLeche_service.countRecepcionLeche();
    }

    //Cantidad leche recibida por año/mes
    @GetMapping("/sumLecheByYearMonth/{year}/{month}")
    public Long sumRecepcionLecheByYearMonth(@PathVariable Integer year, @PathVariable Integer month){
        return recepcionLeche_service.sumRecepcionLecheByYearMonth(year, month);
    }

    //Cantidad de leche recibida entre el rango de fecha de incio y fecha fin con el campo (fechaRecepcion)
    @GetMapping("/sumLecheByDateRange/{fechaInicio}/{fechaFin}")
    public Long sumRecepcionLecheByDateRange(@PathVariable String fechaInicio, @PathVariable String fechaFin){
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        return recepcionLeche_service.sumRecepcionLecheByDateRange(inicio, fin);
    }

    @GetMapping("/sumLecheByDateRangeGroupByFecha/{fechaInicio}/{fechaFin}")
    public List<Object[]> sumRecepcionLecheByDateRangeGroupByFecha(@PathVariable String fechaInicio, @PathVariable String fechaFin){
        LocalDate inicio = LocalDate.parse(fechaInicio);
        LocalDate fin = LocalDate.parse(fechaFin);
        return recepcionLeche_service.sumRecepcionLecheByDateRangeGroupByFecha(inicio, fin);
    }

}
