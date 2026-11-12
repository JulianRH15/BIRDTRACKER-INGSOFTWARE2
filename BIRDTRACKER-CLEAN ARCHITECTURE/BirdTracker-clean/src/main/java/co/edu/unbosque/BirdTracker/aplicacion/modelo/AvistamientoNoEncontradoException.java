package co.edu.unbosque.BirdTracker.aplicacion.modelo;

/**
 * Se lanza cuando se pide un avistamiento que no existe.
 * La aplicación no sabe de códigos HTTP: el adaptador web decide que esto es un 404.
 */
public class AvistamientoNoEncontradoException extends RuntimeException {
    private final Long id;

    public AvistamientoNoEncontradoException(Long id) {
        super("Avistamiento no encontrado");
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
