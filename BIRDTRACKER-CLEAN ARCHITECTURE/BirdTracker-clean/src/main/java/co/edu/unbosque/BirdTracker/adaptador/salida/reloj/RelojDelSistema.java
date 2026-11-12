package co.edu.unbosque.BirdTracker.adaptador.salida.reloj;

import java.time.LocalDate;

import org.springframework.stereotype.Component;

import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.Reloj;

/**
 * Adaptador de salida que conecta el puerto Reloj con el reloj del sistema operativo.
 */
@Component
public class RelojDelSistema implements Reloj {
    @Override
    public LocalDate hoy() {
        return LocalDate.now();
    }
}
