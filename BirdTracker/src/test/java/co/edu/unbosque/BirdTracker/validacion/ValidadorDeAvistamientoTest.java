package co.edu.unbosque.BirdTracker.validacion;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import co.edu.unbosque.BirdTracker.dobles.RelojFijo;
import co.edu.unbosque.BirdTracker.dto.AvistamientoRequest;

class ValidadorDeAvistamientoTest {
    private static final String HOY = "2026-10-21";
    private static final String MANANA = "2026-10-22";

    private ValidadorDeAvistamiento validador;

    @BeforeEach
    void prepararValidadorConRelojFijo() {
        validador = new ValidadorDeAvistamiento(new RelojFijo());
    }

    private static AvistamientoRequest conEspecie(String especie) {
        return new AvistamientoRequest(especie, "Humedal La Conejera", "2026-10-01", "Julián");
    }

    private static AvistamientoRequest conFecha(String fecha) {
        return new AvistamientoRequest("Colibrí", "Humedal La Conejera", fecha, "Julián");
    }

    @Test
    @DisplayName("Especie representativa (7 caracteres) es válida")
    void especieRepresentativa_esValida() {
        AvistamientoRequest request = conEspecie("Colibrí");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Especie de 2 caracteres (justo debajo del mínimo) es inválida")
    void especieDe2Caracteres_esInvalida() {
        AvistamientoRequest request = conEspecie("ab");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("especie");
        assertThat(errores.get("especie")).contains("entre 3 y 50");
    }

    @Test
    @DisplayName("Especie de 3 caracteres (mínimo exacto) es válida")
    void especieDe3Caracteres_esValida() {
        AvistamientoRequest request = conEspecie("abc");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Especie de 50 caracteres (máximo exacto) es válida")
    void especieDe50Caracteres_esValida() {
        AvistamientoRequest request = conEspecie("a".repeat(50));

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Especie de 51 caracteres (justo encima del máximo) es inválida")
    void especieDe51Caracteres_esInvalida() {
        AvistamientoRequest request = conEspecie("a".repeat(51));

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("especie");
    }

    @Test
    @DisplayName("Especie con espacios alrededor se mide sin ellos: '  ab  ' es inválida")
    void especieConEspaciosAlrededor_seMideSinEspacios() {
        AvistamientoRequest request = conEspecie("  ab  ");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("especie");
    }

    @Test
    @DisplayName("Especie ausente (null) es inválida")
    void especieNula_esInvalida() {
        AvistamientoRequest request = conEspecie(null);

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("especie");
    }

    @Test
    @DisplayName("Fecha pasada representativa es válida")
    void fechaPasadaRepresentativa_esValida() {
        AvistamientoRequest request = conFecha("2025-03-15");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Fecha de hoy (límite superior permitido) es válida")
    void fechaDeHoy_esValida() {
        AvistamientoRequest request = conFecha(HOY);

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).isEmpty();
    }

    @Test
    @DisplayName("Fecha de mañana (primer día no permitido) es inválida")
    void fechaDeManana_esInvalida() {
        AvistamientoRequest request = conFecha(MANANA);

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("fecha");
        assertThat(errores.get("fecha")).contains("posterior a hoy");
    }

    @ParameterizedTest(name = "Fecha con formato inválido \"{0}\" es inválida")
    @ValueSource(strings = {"21/10/2026", "2026-1-5", "2026-13-01", "2026-02-30", "ayer"})
    void fechaConFormatoInvalido_esInvalida(String fecha) {
        AvistamientoRequest request = conFecha(fecha);

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("fecha");
        assertThat(errores.get("fecha")).contains("AAAA-MM-DD");
    }

    @Test
    @DisplayName("Fecha vacía es inválida")
    void fechaVacia_esInvalida() {
        AvistamientoRequest request = conFecha("");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("fecha");
    }

    @Test
    @DisplayName("Lugar y observador en blanco se reportan los dos")
    void lugarYObservadorEnBlanco_seReportanAmbos() {
        AvistamientoRequest request = new AvistamientoRequest("Colibrí", "   ", HOY, "");

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("lugar", "observador");
    }

    @Test
    @DisplayName("validar lanza AvistamientoInvalidoException con el nombre del campo que falló")
    void validar_conEspecieInvalida_lanzaExcepcionConElCampo() {
        AvistamientoRequest request = conEspecie("ab");

        AvistamientoInvalidoException e = assertThrows(AvistamientoInvalidoException.class,
                () -> validador.validar(request));

        assertThat(e.getMessage()).contains("especie");
        assertThat(e.getErrores()).containsOnlyKeys("especie");
    }

    @Test
    @DisplayName("Cuerpo nulo es inválido")
    void requestNulo_esInvalido() {
        AvistamientoRequest request = null;

        Map<String, String> errores = validador.errores(request);

        assertThat(errores).containsOnlyKeys("avistamiento");
    }
}
