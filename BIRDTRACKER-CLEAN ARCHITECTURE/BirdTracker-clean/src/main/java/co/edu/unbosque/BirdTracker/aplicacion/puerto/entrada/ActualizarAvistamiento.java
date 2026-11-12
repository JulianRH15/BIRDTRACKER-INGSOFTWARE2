package co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;

/**
 * Puerto de entrada para reemplazar los datos de un avistamiento existente.
 */
public interface ActualizarAvistamiento {
    Avistamiento actualizar(Long id, DatosDeAvistamiento datos);
}
