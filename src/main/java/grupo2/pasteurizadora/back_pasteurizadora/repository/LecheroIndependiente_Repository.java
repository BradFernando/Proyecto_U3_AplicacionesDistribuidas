package grupo2.pasteurizadora.back_pasteurizadora.repository;

import grupo2.pasteurizadora.back_pasteurizadora.entity.LecheroIndependiente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LecheroIndependiente_Repository extends JpaRepository<LecheroIndependiente, String> {

    //Consulta JPQL para saber cuantos datos hay en la tabla
     @Query("SELECT COUNT(l) FROM LecheroIndependiente l")
     Long countLecheroIndependiente();

}
