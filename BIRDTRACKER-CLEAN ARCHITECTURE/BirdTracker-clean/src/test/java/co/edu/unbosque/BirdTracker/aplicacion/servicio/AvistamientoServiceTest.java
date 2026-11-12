package co.edu.unbosque.BirdTracker.aplicacion.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import co.edu.unbosque.BirdTracker.dobles.RelojFijo;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.DatosDeAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.RepositorioDeAvistamientos;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.AvistamientoInvalidoException;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.AvistamientoNoEncontradoException;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ValidadorDeAvistamiento;

class AvistamientoServiceTest {
    private RepositorioDeAvistamientos repositorio;
    private AvistamientoService servicio;

    @BeforeEach
    void prepararServicioConDobles() {
        repositorio = Mockito.mock(RepositorioDeAvistamientos.class);
        servicio = new AvistamientoService(repositorio, new ValidadorDeAvistamiento(new RelojFijo()));
    }

    @Test
    void crear_conDatosValidos_guardaUnaVezElAvistamientoNormalizado() {
        DatosDeAvistamiento request = new DatosDeAvistamiento("  Colibrí ", "Humedal", "2026-10-21", "Julián");
        when(repositorio.guardar(any(Avistamiento.class))).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Avistamiento> guardado = ArgumentCaptor.forClass(Avistamiento.class);

        servicio.crear(request);

        verify(repositorio, times(1)).guardar(guardado.capture());
        assertThat(guardado.getValue().getEspecie()).isEqualTo("Colibrí");
        assertThat(guardado.getValue().getFecha()).isEqualTo(LocalDate.of(2026, 10, 21));
    }

    @Test
    void crear_conFechaDeManana_noLlamaAlRepositorio() {
        DatosDeAvistamiento request = new DatosDeAvistamiento("Colibrí", "Humedal", "2026-10-22", "Julián");

        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> servicio.crear(request));

        assertThat(e.getErrores()).containsOnlyKeys("fecha");
        verify(repositorio, never()).guardar(any());
    }

    @Test
    void actualizar_conIdInexistente_lanza404YNoGuarda() {
        DatosDeAvistamiento request = new DatosDeAvistamiento("Colibrí", "Humedal", "2026-10-01", "Julián");
        when(repositorio.buscarPorId(99L)).thenReturn(Optional.empty());

        AvistamientoNoEncontradoException e = assertThrows(AvistamientoNoEncontradoException.class,
                () -> servicio.actualizar(99L, request));

        assertThat(e.getId()).isEqualTo(99L);
        verify(repositorio, never()).guardar(any());
    }

    @Test
    void actualizar_conDatosInvalidos_noConsultaNiGuarda() {
        DatosDeAvistamiento request = new DatosDeAvistamiento("ab", "Humedal", "2026-10-01", "Julián");

        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> servicio.actualizar(1L, request));

        assertThat(e.getErrores()).containsOnlyKeys("especie");
        verify(repositorio, never()).buscarPorId(any());
        verify(repositorio, never()).guardar(any());
    }
}
