package co.edu.unbosque.BirdTracker.aplicacion.modelo;

import java.time.LocalDate;

/**
 * Un avistamiento de ave tal como lo entiende la aplicación.
 * No sabe nada de JPA, de JSON ni de Spring: eso queda en los adaptadores.
 */
public class Avistamiento {
    private final Long id;
    private final String especie;
    private final String lugar;
    private final LocalDate fecha;
    private final String observador;

    public Avistamiento(Long id, String especie, String lugar, LocalDate fecha, String observador) {
        this.id = id;
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    public Long getId() { return id; }
    public String getEspecie() { return especie; }
    public String getLugar() { return lugar; }
    public LocalDate getFecha() { return fecha; }
    public String getObservador() { return observador; }
}
