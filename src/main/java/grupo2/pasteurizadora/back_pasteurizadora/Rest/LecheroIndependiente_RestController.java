package grupo2.pasteurizadora.back_pasteurizadora.Rest;

import grupo2.pasteurizadora.back_pasteurizadora.entity.LecheroIndependiente;
import grupo2.pasteurizadora.back_pasteurizadora.services.LecheroIndependiente_Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = {"http://localhost:4200"})
@RestController
@RequestMapping("/lecheroIndependiente")
public class LecheroIndependiente_RestController {

    final
    LecheroIndependiente_Service lecheroIndependiente_service;

    public LecheroIndependiente_RestController(LecheroIndependiente_Service lecheroIndependiente_service) {
        this.lecheroIndependiente_service = lecheroIndependiente_service;
    }

    @GetMapping
    public List<LecheroIndependiente> getAll() {
        return lecheroIndependiente_service.getAll();
    }

    @GetMapping("/{codLechero}")
    public LecheroIndependiente getLecheroIndependienteByCodLechero(@PathVariable String codLechero) {
        return lecheroIndependiente_service.getLecheroIndependienteByCodLechero(codLechero);
    }

    @PostMapping
    public LecheroIndependiente saveLecheroIndependiente(@RequestBody LecheroIndependiente lecheroIndependiente) {
        return lecheroIndependiente_service.saveLecheroIndependiente(lecheroIndependiente);
    }

    @PutMapping("/{codLechero}")
    public LecheroIndependiente updateLecheroIndependiente(@PathVariable String codLechero, @RequestBody LecheroIndependiente lecheroIndependiente) {
        lecheroIndependiente.setCodLechero(codLechero);
        return lecheroIndependiente_service.updateLecheroIndependiente(lecheroIndependiente);
    }

    @DeleteMapping("/{codLechero}")
    public void deleteLecheroIndependiente(@PathVariable String codLechero) {
        lecheroIndependiente_service.deleteLecheroIndependiente(codLechero);
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @GetMapping("/count")
    public Long countLecheroIndependiente() {
        return lecheroIndependiente_service.countLecheroIndependiente();
    }

}
