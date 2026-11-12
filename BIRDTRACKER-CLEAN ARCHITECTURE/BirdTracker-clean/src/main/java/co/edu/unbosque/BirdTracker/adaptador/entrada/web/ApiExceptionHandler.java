package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.AvistamientoInvalidoException;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.AvistamientoNoEncontradoException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidacion(MethodArgumentNotValidException exception) {
        Map<String, String> errores = exception.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage,
                        (primero, ignorado) -> primero, LinkedHashMap::new));
        return respuesta(HttpStatus.BAD_REQUEST, "Datos inválidos", errores);
    }

    @ExceptionHandler(AvistamientoInvalidoException.class)
    public ResponseEntity<Map<String, Object>> manejarReglaDeNegocio(AvistamientoInvalidoException exception) {
        return respuesta(HttpStatus.BAD_REQUEST, exception.getMessage(), exception.getErrores());
    }

    @ExceptionHandler(AvistamientoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarNoEncontrado(AvistamientoNoEncontradoException exception) {
        return respuesta(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> manejarJsonInvalido(HttpMessageNotReadableException exception) {
        return respuesta(HttpStatus.BAD_REQUEST, "JSON inválido o mal formado", null);
    }

    private ResponseEntity<Map<String, Object>> respuesta(HttpStatus status, String mensaje, Object detalles) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", status.value());
        cuerpo.put("error", status.getReasonPhrase());
        cuerpo.put("mensaje", mensaje);
        if (detalles != null) cuerpo.put("detalles", detalles);
        return ResponseEntity.status(status).body(cuerpo);
    }
}
