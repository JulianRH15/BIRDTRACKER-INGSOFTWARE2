package co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada;

import java.util.List;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ResumenEspecie;

/**
 * Puerto de entrada para leer avistamientos: todos, uno por ID o el conteo por especie.
 */
public interface ConsultarAvistamientos {
    List<Avistamiento> listar();

    Avistamiento buscar(Long id);

    List<ResumenEspecie> resumen();
}
