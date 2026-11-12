package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.DatosDeAvistamiento;

/**
 * Cuerpo JSON que recibe la API. El adaptador web lo traduce a los datos que entiende la aplicación.
 */
public record AvistamientoRequest(String especie, String lugar, String fecha, String observador) {

    public DatosDeAvistamiento aDatos() {
        return new DatosDeAvistamiento(especie, lugar, fecha, observador);
    }
}
