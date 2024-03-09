package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.entity.HaciendaLechera;
import grupo2.pasteurizadora.back_pasteurizadora.services.HaciendaLechera_Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/haciendaLechera")
public class HaciendaLechera_RestController {

    final
    HaciendaLechera_Service haciendaLechera_service;

    public HaciendaLechera_RestController(HaciendaLechera_Service haciendaLechera_service) {
        this.haciendaLechera_service = haciendaLechera_service;
    }

    @GetMapping
    public List<HaciendaLechera> getAll() {
        return haciendaLechera_service.getAll();
    }

    @GetMapping("/{codHacienda}")
    public HaciendaLechera getHaciendaLecheraByCodHacienda(@PathVariable String codHacienda) {
        return haciendaLechera_service.getHaciendaLecheraByCodHacienda(codHacienda);
    }

    @PostMapping
    public HaciendaLechera saveHaciendaLechera(@RequestBody HaciendaLechera haciendaLechera) {
        return haciendaLechera_service.saveHaciendaLechera(haciendaLechera);
    }

    @PutMapping("/{codHacienda}")
    public HaciendaLechera updateHaciendaLechera(@PathVariable String codHacienda, @RequestBody HaciendaLechera haciendaLechera) {
        haciendaLechera.setCodHacienda(codHacienda);
        return haciendaLechera_service.updateHaciendaLechera(haciendaLechera);
    }

    @DeleteMapping("/{codHacienda}")
    public void deleteHaciendaLechera(@PathVariable String codHacienda) {
        haciendaLechera_service.deleteHaciendaLechera(codHacienda);
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countHaciendaLechera() {
        return haciendaLechera_service.countHaciendaLechera();
    }

}
