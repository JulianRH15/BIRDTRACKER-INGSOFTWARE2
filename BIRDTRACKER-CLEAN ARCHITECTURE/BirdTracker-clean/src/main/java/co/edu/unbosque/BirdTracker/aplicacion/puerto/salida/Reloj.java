package co.edu.unbosque.BirdTracker.aplicacion.puerto.salida;

import java.time.LocalDate;

/**
 * Puerto de salida: la aplicación le pregunta la fecha de hoy a algo que está afuera.
 */
public interface Reloj {
    LocalDate hoy();
}
