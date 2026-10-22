package co.edu.unbosque.BirdTracker.dobles;

import java.time.LocalDate;

import co.edu.unbosque.BirdTracker.validacion.Reloj;

public class RelojFijo implements Reloj {
    public static final LocalDate HOY = LocalDate.of(2026, 10, 21);

    private final LocalDate hoy;

    public RelojFijo() {
        this(HOY);
    }

    public RelojFijo(LocalDate hoy) {
        this.hoy = hoy;
    }

    @Override
    public LocalDate hoy() {
        return hoy;
    }
}
