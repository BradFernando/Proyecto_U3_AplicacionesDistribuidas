package grupo2.pasteurizadora.back_pasteurizadora.services;

import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepcionHaciendasLecheras_dto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepcionLecheUpdateDto;
import grupo2.pasteurizadora.back_pasteurizadora.dto.RecepciondeLechero_dto;
import grupo2.pasteurizadora.back_pasteurizadora.entity.HaciendaLechera;
import grupo2.pasteurizadora.back_pasteurizadora.entity.LecheroIndependiente;
import grupo2.pasteurizadora.back_pasteurizadora.entity.RecepcionLeche;
import grupo2.pasteurizadora.back_pasteurizadora.exception.ClientException;
import grupo2.pasteurizadora.back_pasteurizadora.repository.HaciendaLechera_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.LecheroIndependiente_Repository;
import grupo2.pasteurizadora.back_pasteurizadora.repository.RecepcionLeche_Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecepcionLeche_Service {

    final RecepcionLeche_Repository recepcionLeche_repository;
    final HaciendaLechera_Repository haciendaLechera_repository;
    final LecheroIndependiente_Repository lecheroIndependiente_repository;

    @Autowired
    public RecepcionLeche_Service(RecepcionLeche_Repository recepcionLeche_repository, HaciendaLechera_Repository haciendaLechera_repository, LecheroIndependiente_Repository lecheroIndependiente_repository) {
        this.recepcionLeche_repository = recepcionLeche_repository;
        this.haciendaLechera_repository = haciendaLechera_repository;
        this.lecheroIndependiente_repository = lecheroIndependiente_repository;
    }

    public RecepcionLeche saveRecepcionLecheroIndependiente(RecepciondeLechero_dto recepciondeLechero_dto) {
        RecepcionLeche recepcionLeche = new RecepcionLeche();
        recepcionLeche.setCodRecepcion(recepciondeLechero_dto.getCodRecepcion());
        recepcionLeche.setFechaRecepcion(recepciondeLechero_dto.getFechaRecepcion());
        recepcionLeche.setResultadosPruebasCalidad(recepciondeLechero_dto.getResultadosPruebasCalidad());
        recepcionLeche.setCantidadLecheRecibida(recepciondeLechero_dto.getCantidadLecheRecibida());
        recepcionLeche.setPagoTotal(recepciondeLechero_dto.getPagoTotal());

        List<LecheroIndependiente> lecheroIndependiente = recepciondeLechero_dto.getLecheroIndependiente().stream()
                .map(lecheroIndependiente_repository::findById)
                .map(optionalLechero -> optionalLechero.orElseThrow(() -> new ClientException("No se encontró LecheroIndependiente con el ID proporcionado")))
                .collect(Collectors.toList());

        for (LecheroIndependiente lechero : lecheroIndependiente) {
            lechero.setRecepcionLeche(recepcionLeche);
        }

        recepcionLeche.setLecheroIndependiente(lecheroIndependiente);

        return recepcionLeche_repository.save(recepcionLeche);
    }

    public RecepcionLeche saveRecepcionHaciendaLechera(RecepcionHaciendasLecheras_dto recepcionHaciendasLecheras_dto) {
        RecepcionLeche recepcionLeche = new RecepcionLeche();
        recepcionLeche.setCodRecepcion(recepcionHaciendasLecheras_dto.getCodRecepcion());
        recepcionLeche.setFechaRecepcion(recepcionHaciendasLecheras_dto.getFechaRecepcion());
        recepcionLeche.setResultadosPruebasCalidad(recepcionHaciendasLecheras_dto.getResultadosPruebasCalidad());
        recepcionLeche.setCantidadLecheRecibida(recepcionHaciendasLecheras_dto.getCantidadLecheRecibida());
        recepcionLeche.setPagoTotal(recepcionHaciendasLecheras_dto.getPagoTotal());

        List<HaciendaLechera> haciendaLechera = recepcionHaciendasLecheras_dto.getHaciendaLechera().stream()
                .map(haciendaLechera_repository::findById)
                .map(optionalHacienda -> optionalHacienda.orElseThrow(() -> new ClientException("No se encontró HaciendaLechera con el ID proporcionado")))
                .collect(Collectors.toList());

        for (HaciendaLechera hacienda : haciendaLechera) {
            hacienda.setRecepcionLeche(recepcionLeche);
        }

        recepcionLeche.setHaciendaLechera(haciendaLechera);

        return recepcionLeche_repository.save(recepcionLeche);
    }

    public List<RecepcionLeche> getAllRecepcionLeche() {
        return recepcionLeche_repository.findAll();
    }

    public RecepcionLeche getRecepcionLecheById(String codRecepcion) {
        return recepcionLeche_repository.findById(codRecepcion).orElseThrow(() -> new ClientException("No se encontró RecepcionLeche con el ID proporcionado"));
    }

    public RecepcionLeche updateRecepcionLeche(RecepcionLecheUpdateDto recepcionLecheDto) {
        Optional<RecepcionLeche> existingRecepcionLecheOptional = recepcionLeche_repository.findById(recepcionLecheDto.getCodRecepcion());
        if (existingRecepcionLecheOptional.isEmpty()) {
            throw new ClientException("No se encontró la recepción de leche con el código: " + recepcionLecheDto.getCodRecepcion());
        }
        RecepcionLeche existingRecepcionLeche = existingRecepcionLecheOptional.get();
        existingRecepcionLeche.setFechaRecepcion(recepcionLecheDto.getFechaRecepcion());
        existingRecepcionLeche.setResultadosPruebasCalidad(recepcionLecheDto.getResultadosPruebasCalidad());
        existingRecepcionLeche.setCantidadLecheRecibida(recepcionLecheDto.getCantidadLecheRecibida());
        existingRecepcionLeche.setPagoTotal(recepcionLecheDto.getPagoTotal());

        return recepcionLeche_repository.save(existingRecepcionLeche);
    }
    public void deleteRecepcionLeche(String codRecepcion) {
        Optional<RecepcionLeche> recepcionLecheOptional = recepcionLeche_repository.findById(codRecepcion);
        if (recepcionLecheOptional.isPresent()) {
            RecepcionLeche recepcionLeche = recepcionLecheOptional.get();

            // Desvincula y guarda las entidades HaciendaLechera y LecheroIndependiente
            for (HaciendaLechera hacienda : recepcionLeche.getHaciendaLechera()) {
                hacienda.setRecepcionLeche(null);
                haciendaLechera_repository.save(hacienda);
            }
            for (LecheroIndependiente lechero : recepcionLeche.getLecheroIndependiente()) {
                lechero.setRecepcionLeche(null);
                lecheroIndependiente_repository.save(lechero);
            }

            // Elimina la RecepcionLeche de la base de datos
            recepcionLeche_repository.deleteById(codRecepcion);
        } else {
            throw new ClientException("No se encontró la recepción de leche con el código: " + codRecepcion);
        }
    }


    //Consultas JPQL
    public List<String> findCodLechero(){
        return recepcionLeche_repository.findCodLechero();
    }

    public List<String> findCodHacienda(){
        return recepcionLeche_repository.findCodHacienda();
    }

    public List<String> findCodLecheroSinRecepcion(){
        return recepcionLeche_repository.findCodLecheroSinRecepcion();
    }

    public List<String> findCodHaciendaSinRecepcion(){
        return recepcionLeche_repository.findCodHaciendaSinRecepcion();
    }

    //Consulta JPQL para saber cuantos datos hay en la tabla
    public Long countRecepcionLeche() {
        return recepcionLeche_repository.countRecepcionLeche();
    }
    //Cantidad leche recibida por año/mes
    public Long sumRecepcionLecheByYearMonth(Integer year, Integer month) {
        return recepcionLeche_repository.sumRecepcionLecheByYearMonth(year, month);
    }

    //Cantidad de leche recibida entre el rango de fecha de incio y fecha fin con el campo (fechaRecepcion)
    public Long sumRecepcionLecheByDateRange(LocalDate fechaInicio, LocalDate fechaFin) {
        return recepcionLeche_repository.sumRecepcionLecheByDateRange(fechaInicio, fechaFin);
    }

    public List<Object[]> sumRecepcionLecheByDateRangeGroupByFecha(LocalDate fechaInicio, LocalDate fechaFin) {
        return recepcionLeche_repository.sumRecepcionLecheByDateRangeGroupByFecha(fechaInicio, fechaFin);
    }


}
