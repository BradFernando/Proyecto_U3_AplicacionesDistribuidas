package grupo2.pasteurizadora.back_pasteurizadora.repository;

import grupo2.pasteurizadora.back_pasteurizadora.entity.HaciendaLechera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HaciendaLechera_Repository extends JpaRepository<HaciendaLechera, String> {

    //Consulta JPQL para saber cuantos datos hay en la tabla
    @Query("SELECT COUNT(h) FROM HaciendaLechera h")
    Long countHaciendaLechera();

}
