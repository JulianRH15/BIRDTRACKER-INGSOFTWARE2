package co.edu.unbosque.BirdTracker.validacion;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class AvistamientoInvalidoException extends RuntimeException {
    private final Map<String, String> errores;

    public AvistamientoInvalidoException(Map<String, String> errores) {
        super("Datos inválidos en: " + String.join(", ", errores.keySet()));
        this.errores = Collections.unmodifiableMap(new LinkedHashMap<>(errores));
    }

    public Map<String, String> getErrores() {
        return errores;
    }
}
