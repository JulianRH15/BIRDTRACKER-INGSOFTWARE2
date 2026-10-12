package co.edu.unbosque.BirdTracker.controller;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;

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

import co.edu.unbosque.BirdTracker.model.Avistamiento;
import co.edu.unbosque.BirdTracker.repository.AvistamientoRepository;
import co.edu.unbosque.BirdTracker.service.AvistamientoService;

@RestController
@RequestMapping(path = "/avistamientos", produces = "application/json")
@Tag(name = "Avistamientos", description = "Registro y consulta de avistamientos de aves")
public class AvistamientoController {

    private final AvistamientoService service;

    public AvistamientoController(AvistamientoService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar todos los avistamientos")
    public ResponseEntity<List<Avistamiento>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/resumen")
    @Operation(summary = "Contar avistamientos por especie")
    public ResponseEntity<List<AvistamientoRepository.ResumenEspecie>> resumen() {
        return ResponseEntity.ok(service.resumen());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar un avistamiento por su ID")
    public ResponseEntity<Avistamiento> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscar(id));
    }

    @PostMapping(consumes = "application/json")
    @Operation(summary = "Registrar un avistamiento")
    public ResponseEntity<Avistamiento> crear(@Valid @RequestBody Avistamiento avistamiento) {
        Avistamiento creado = service.crear(avistamiento);
        return ResponseEntity.created(URI.create("/avistamientos/" + creado.getId())).body(creado);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @Operation(summary = "Reemplazar un avistamiento")
    public ResponseEntity<Avistamiento> actualizar(@PathVariable Long id,
            @Valid @RequestBody Avistamiento avistamiento) {
        return ResponseEntity.ok(service.actualizar(id, avistamiento));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un avistamiento")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
