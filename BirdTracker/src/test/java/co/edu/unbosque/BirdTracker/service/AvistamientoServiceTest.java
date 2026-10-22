package co.edu.unbosque.BirdTracker.service;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.BirdTracker.dobles.RelojFijo;
import co.edu.unbosque.BirdTracker.dto.AvistamientoRequest;
import co.edu.unbosque.BirdTracker.model.Avistamiento;
import co.edu.unbosque.BirdTracker.repository.AvistamientoRepository;
import co.edu.unbosque.BirdTracker.validacion.AvistamientoInvalidoException;
import co.edu.unbosque.BirdTracker.validacion.ValidadorDeAvistamiento;

class AvistamientoServiceTest {
    private AvistamientoRepository repositorio;
    private AvistamientoService servicio;

    @BeforeEach
    void prepararServicioConDobles() {
        repositorio = Mockito.mock(AvistamientoRepository.class);
        servicio = new AvistamientoService(repositorio, new ValidadorDeAvistamiento(new RelojFijo()));
    }

    @Test
    void crear_conDatosValidos_guardaUnaVezElAvistamientoNormalizado() {
        AvistamientoRequest request = new AvistamientoRequest("  Colibrí ", "Humedal", "2026-10-21", "Julián");
        when(repositorio.save(any(Avistamiento.class))).thenAnswer(inv -> inv.getArgument(0));
        ArgumentCaptor<Avistamiento> guardado = ArgumentCaptor.forClass(Avistamiento.class);

        servicio.crear(request);

        verify(repositorio, times(1)).save(guardado.capture());
        assertThat(guardado.getValue().getEspecie()).isEqualTo("Colibrí");
        assertThat(guardado.getValue().getFecha()).isEqualTo(LocalDate.of(2026, 10, 21));
    }

    @Test
    void crear_conFechaDeManana_noLlamaAlRepositorio() {
        AvistamientoRequest request = new AvistamientoRequest("Colibrí", "Humedal", "2026-10-22", "Julián");

        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> servicio.crear(request));

        assertThat(e.getErrores()).containsOnlyKeys("fecha");
        verify(repositorio, never()).save(any());
    }

    @Test
    void actualizar_conIdInexistente_lanza404YNoGuarda() {
        AvistamientoRequest request = new AvistamientoRequest("Colibrí", "Humedal", "2026-10-01", "Julián");
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException e = assertThrows(ResponseStatusException.class,
                () -> servicio.actualizar(99L, request));

        assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(repositorio, never()).save(any());
    }

    @Test
    void actualizar_conDatosInvalidos_noConsultaNiGuarda() {
        AvistamientoRequest request = new AvistamientoRequest("ab", "Humedal", "2026-10-01", "Julián");

        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> servicio.actualizar(1L, request));

        assertThat(e.getErrores()).containsOnlyKeys("especie");
        verify(repositorio, never()).findById(any());
        verify(repositorio, never()).save(any());
    }
}
