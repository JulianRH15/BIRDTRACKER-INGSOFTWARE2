package co.edu.unbosque.BirdTracker.adaptador.entrada.web;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.ActualizarAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.ConsultarAvistamientos;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.CrearAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.EliminarAvistamiento;

/**
 * Adaptador de entrada HTTP. Traduce peticiones REST a llamadas sobre los puertos de entrada
 * y traduce lo que devuelve la aplicación a JSON.
 */
@RestController
@RequestMapping(path = "/avistamientos", produces = "application/json")
@Tag(name = "Avistamientos", description = "Registro y consulta de avistamientos de aves")
public class AvistamientoController {
    private final CrearAvistamiento crear;
    private final ConsultarAvistamientos consultar;
    private final ActualizarAvistamiento actualizar;
    private final EliminarAvistamiento eliminar;

    public AvistamientoController(CrearAvistamiento crear, ConsultarAvistamientos consultar,
            ActualizarAvistamiento actualizar, EliminarAvistamiento eliminar) {
        this.crear = crear;
        this.consultar = consultar;
        this.actualizar = actualizar;
        this.eliminar = eliminar;
    }

    @GetMapping
    @Operation(summary = "Listar todos los avistamientos")
    public ResponseEntity<List<AvistamientoResponse>> listar() {
        return ResponseEntity.ok(consultar.listar().stream().map(AvistamientoResponse::desde).toList());
    }

    @GetMapping("/resumen")
    @Operation(summary = "Contar avistamientos por especie")
    public ResponseEntity<List<ResumenEspecieResponse>> resumen() {
        return ResponseEntity.ok(consultar.resumen().stream().map(ResumenEspecieResponse::desde).toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un avistamiento por su ID")
    public ResponseEntity<AvistamientoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(AvistamientoResponse.desde(consultar.buscar(id)));
    }

    @PostMapping(consumes = "application/json")
    @Operation(summary = "Registrar un avistamiento")
    public ResponseEntity<AvistamientoResponse> crear(@RequestBody AvistamientoRequest avistamiento) {
        AvistamientoResponse creado = AvistamientoResponse.desde(crear.crear(avistamiento.aDatos()));
        return ResponseEntity.created(URI.create("/avistamientos/" + creado.id())).body(creado);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @Operation(summary = "Reemplazar un avistamiento")
    public ResponseEntity<AvistamientoResponse> actualizar(@PathVariable Long id,
            @RequestBody AvistamientoRequest avistamiento) {
        return ResponseEntity.ok(AvistamientoResponse.desde(actualizar.actualizar(id, avistamiento.aDatos())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un avistamiento")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eliminar.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
