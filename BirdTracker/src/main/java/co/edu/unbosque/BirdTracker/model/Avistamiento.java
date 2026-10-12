package co.edu.unbosque.BirdTracker.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "avistamientos")
public class Avistamiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La especie es obligatoria")
    @Size(max = 120, message = "La especie no puede superar 120 caracteres")
    private String especie;

    @NotBlank(message = "El lugar es obligatorio")
    @Size(max = 200, message = "El lugar no puede superar 200 caracteres")
    private String lugar;

    @NotNull(message = "La fecha es obligatoria y debe tener formato AAAA-MM-DD")
    private LocalDate fecha;

    @NotBlank(message = "El observador es obligatorio")
    @Size(max = 120, message = "El observador no puede superar 120 caracteres")
    private String observador;

    protected Avistamiento() {
    }

    public Avistamiento(String especie, String lugar, LocalDate fecha, String observador) {
        this.especie = especie;
        this.lugar = lugar;
        this.fecha = fecha;
        this.observador = observador;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }
    public String getLugar() { return lugar; }
    public void setLugar(String lugar) { this.lugar = lugar; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public String getObservador() { return observador; }
    public void setObservador(String observador) { this.observador = observador; }
}
