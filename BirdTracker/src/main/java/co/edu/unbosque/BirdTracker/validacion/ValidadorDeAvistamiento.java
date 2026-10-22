package co.edu.unbosque.BirdTracker.validacion;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import co.edu.unbosque.BirdTracker.dto.AvistamientoRequest;

@Component
public class ValidadorDeAvistamiento {
    public static final int ESPECIE_MIN = 3;
    public static final int ESPECIE_MAX = 50;

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("uuuu-MM-dd").withResolverStyle(ResolverStyle.STRICT);

    private final Reloj reloj;

    public ValidadorDeAvistamiento(Reloj reloj) {
        this.reloj = reloj;
    }

    public void validar(AvistamientoRequest request) {
        Map<String, String> errores = errores(request);
        if (!errores.isEmpty()) {
            throw new AvistamientoInvalidoException(errores);
        }
    }

    public Map<String, String> errores(AvistamientoRequest request) {
        Map<String, String> errores = new LinkedHashMap<>();
        if (request == null) {
            errores.put("avistamiento", "El cuerpo de la petición es obligatorio");
            return errores;
        }
        validarEspecie(request.especie(), errores);
        validarFecha(request.fecha(), errores);
        if (estaVacio(request.lugar())) {
            errores.put("lugar", "El lugar es obligatorio");
        }
        if (estaVacio(request.observador())) {
            errores.put("observador", "El observador es obligatorio");
        }
        return errores;
    }

    public static LocalDate convertirFecha(String fecha) {
        return LocalDate.parse(fecha, FORMATO_FECHA);
    }

    private void validarEspecie(String especie, Map<String, String> errores) {
        int longitud = especie == null ? 0 : especie.strip().length();
        if (longitud < ESPECIE_MIN || longitud > ESPECIE_MAX) {
            errores.put("especie", "La especie debe tener entre " + ESPECIE_MIN + " y " + ESPECIE_MAX
                    + " caracteres (tiene " + longitud + ")");
        }
    }

    private void validarFecha(String fecha, Map<String, String> errores) {
        if (estaVacio(fecha)) {
            errores.put("fecha", "La fecha es obligatoria y debe tener formato AAAA-MM-DD");
            return;
        }
        LocalDate valor;
        try {
            valor = convertirFecha(fecha.strip());
        } catch (DateTimeParseException e) {
            errores.put("fecha", "La fecha debe tener formato AAAA-MM-DD y ser una fecha real");
            return;
        }
        if (valor.isAfter(reloj.hoy())) {
            errores.put("fecha", "La fecha no puede ser posterior a hoy");
        }
    }

    private static boolean estaVacio(String valor) {
        return valor == null || valor.isBlank();
    }
}
