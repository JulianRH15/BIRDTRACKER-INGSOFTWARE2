package co.edu.unbosque.BirdTracker.adaptador.salida.persistencia;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repositorio de Spring Data. Solo lo usa el adaptador de persistencia.
 */
public interface AvistamientoJpaRepository extends JpaRepository<AvistamientoJpa, Long> {

    @Query("select a.especie as especie, count(a) as cantidad from Avistamiento a group by a.especie order by a.especie")
    List<FilaResumen> resumirPorEspecie();

    interface FilaResumen {
        String getEspecie();
        long getCantidad();
    }
}
