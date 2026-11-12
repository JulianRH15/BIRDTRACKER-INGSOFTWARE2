package co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada;

/**
 * Datos que llegan por un puerto de entrada para registrar o reemplazar un avistamiento.
 * Vienen tal cual los escribió el usuario: la aplicación los valida antes de usarlos.
 */
public record DatosDeAvistamiento(String especie, String lugar, String fecha, String observador) {
}
