package co.edu.unbosque.BirdTracker.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unbosque.BirdTracker.model.Avistamiento;

public interface AvistamientoRepository extends JpaRepository<Avistamiento, Long> {

    @Query("select a.especie as especie, count(a) as cantidad from Avistamiento a group by a.especie order by a.especie")
    List<ResumenEspecie> resumirPorEspecie();

    interface ResumenEspecie {
        String getEspecie();
        long getCantidad();
    }
}
