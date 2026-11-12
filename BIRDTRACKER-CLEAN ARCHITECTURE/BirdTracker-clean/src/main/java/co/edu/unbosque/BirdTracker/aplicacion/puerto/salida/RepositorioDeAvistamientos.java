package co.edu.unbosque.BirdTracker.aplicacion.puerto.salida;

import java.util.List;
import java.util.Optional;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ResumenEspecie;

/**
 * Puerto de salida para guardar y leer avistamientos.
 * La aplicación define lo que necesita y un adaptador decide cómo y dónde se guarda.
 */
public interface RepositorioDeAvistamientos {
    List<Avistamiento> listarTodos();

    Optional<Avistamiento> buscarPorId(Long id);

    Avistamiento guardar(Avistamiento avistamiento);

    void eliminar(Avistamiento avistamiento);

    List<ResumenEspecie> resumirPorEspecie();
}
