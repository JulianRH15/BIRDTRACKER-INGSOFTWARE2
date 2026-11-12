package co.edu.unbosque.BirdTracker.configuracion;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.ValidadorDeAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.Reloj;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.BirdTracker.aplicacion.servicio.AvistamientoService;

/**
 * Arma la aplicación: conecta cada puerto de salida con el adaptador que Spring encuentre
 * y publica el servicio para que los adaptadores de entrada lo usen por sus puertos.
 * Es el único sitio que conoce a la vez el núcleo y el framework.
 */
@Configuration
public class ConfiguracionDeAvistamientos {

    @Bean
    ValidadorDeAvistamiento validadorDeAvistamiento(Reloj reloj) {
        return new ValidadorDeAvistamiento(reloj);
    }

    @Bean
    AvistamientoService avistamientoService(RepositorioDeAvistamientos repositorio,
            ValidadorDeAvistamiento validador) {
        return new AvistamientoService(repositorio, validador);
    }
}
