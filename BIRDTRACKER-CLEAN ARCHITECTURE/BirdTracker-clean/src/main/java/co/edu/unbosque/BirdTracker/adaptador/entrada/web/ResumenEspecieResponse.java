package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import io.swagger.v3.oas.annotations.media.Schema;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.ResumenEspecie;

/**
 * JSON del endpoint /avistamientos/resumen.
 */
@Schema(name = "ResumenEspecie")
public record ResumenEspecieResponse(String especie, long cantidad) {

    static ResumenEspecieResponse desde(ResumenEspecie resumen) {
        return new ResumenEspecieResponse(resumen.especie(), resumen.cantidad());
    }
}
