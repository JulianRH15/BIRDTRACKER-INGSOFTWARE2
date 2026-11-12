package co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;

/**
 * Puerto de entrada para registrar un avistamiento nuevo.
 */
public interface CrearAvistamiento {
    Avistamiento crear(DatosDeAvistamiento datos);
}
