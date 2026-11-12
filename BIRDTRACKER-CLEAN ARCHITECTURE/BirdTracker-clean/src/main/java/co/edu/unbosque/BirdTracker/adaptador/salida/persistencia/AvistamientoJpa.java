package co.edu.unbosque.BirdTracker.adaptador.salida.persistencia;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;

/**
 * Fila de la tabla avistamientos. Es un detalle del adaptador de persistencia,
 * por eso las anotaciones de JPA viven aquí y no en el modelo de la aplicación.
 */
@Entity(name = "Avistamiento")
@Table(name = "avistamientos")
public class AvistamientoJpa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La especie es obligatoria")
    @Size(min = 3, max = 50, message = "La especie debe tener entre 3 y 50 caracteres")
    private String especie;

    @NotBlank(message = "El lugar es obligatorio")
    @Size(max = 200, message = "El lugar no puede superar 200 caracteres")
    private String lugar;

    @NotNull(message = "La fecha es obligatoria y debe tener formato AAAA-MM-DD")
    private LocalDate fecha;

    @NotBlank(message = "El observador es obligatorio")
    @Size(max = 120, message = "El observador no puede superar 120 caracteres")
    private String observador;

    protected AvistamientoJpa() {
    }

    public AvistamientoJpa(String especie, String lugar, LocalDate fecha, String observador) {
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    static AvistamientoJpa desde(Avistamiento avistamiento) {
        AvistamientoJpa fila = new AvistamientoJpa(avistamiento.getEspecie(), avistamiento.getLugar(),
                avistamiento.getFecha(), avistamiento.getObservador());
        fila.id = avistamiento.getId();
        return fila;
    }

    Avistamiento aModelo() {
        return new Avistamiento(id, especie, lugar, fecha, observador);
    }

    public Long getId() { return id; }
    public String getEspecie() { return especie; }
    public String getLugar() { return lugar; }
    public LocalDate getFecha() { return fecha; }
    public String getObservador() { return observador; }
}
