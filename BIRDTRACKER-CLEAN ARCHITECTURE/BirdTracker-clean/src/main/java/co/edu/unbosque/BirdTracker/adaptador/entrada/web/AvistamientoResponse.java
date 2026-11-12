package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;

/**
 * JSON que devuelve la API para un avistamiento. Las anotaciones solo sirven para que Swagger
 * siga mostrando el mismo esquema que mostraba cuando la API devolvía la entidad JPA.
 */
@Schema(name = "Avistamiento")
public record AvistamientoResponse(
        Long id,
        @NotBlank @Size(min = 3, max = 50) String especie,
        @NotBlank @Size(max = 200) String lugar,
        @NotNull LocalDate fecha,
        @NotBlank @Size(max = 120) String observador) {

    static AvistamientoResponse desde(Avistamiento avistamiento) {
        return new AvistamientoResponse(avistamiento.getId(), avistamiento.getEspecie(), avistamiento.getLugar(),
                avistamiento.getFecha(), avistamiento.getObservador());
    }
}
