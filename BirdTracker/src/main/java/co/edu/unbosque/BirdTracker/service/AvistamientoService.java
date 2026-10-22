package co.edu.unbosque.BirdTracker.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.BirdTracker.dto.AvistamientoRequest;
import co.edu.unbosque.BirdTracker.model.Avistamiento;
import co.edu.unbosque.BirdTracker.repository.AvistamientoRepository;
import co.edu.unbosque.BirdTracker.validacion.ValidadorDeAvistamiento;

@Service
@Transactional
public class AvistamientoService {
    private final AvistamientoRepository repository;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoService(AvistamientoRepository repository, ValidadorDeAvistamiento validador) {
        this.repository = repository;
        this.validador = validador;
    }

    @Transactional(readOnly = true)
    public List<Avistamiento> listar() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Avistamiento buscar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Avistamiento no encontrado"));
    }

    public Avistamiento crear(AvistamientoRequest request) {
        validador.validar(request);
        Avistamiento nuevo = new Avistamiento(request.especie().strip(), request.lugar().strip(),
                ValidadorDeAvistamiento.convertirFecha(request.fecha().strip()), request.observador().strip());
        return repository.save(nuevo);
    }

    public Avistamiento actualizar(Long id, AvistamientoRequest request) {
        validador.validar(request);
        Avistamiento existente = buscar(id);
        existente.setEspecie(request.especie().strip());
        existente.setLugar(request.lugar().strip());
        existente.setFecha(ValidadorDeAvistamiento.convertirFecha(request.fecha().strip()));
        existente.setObservador(request.observador().strip());
        return repository.save(existente);
    }

    public void eliminar(Long id) {
        Avistamiento existente = buscar(id);
        repository.delete(existente);
    }

    @Transactional(readOnly = true)
    public List<AvistamientoRepository.ResumenEspecie> resumen() {
        return repository.resumirPorEspecie();
    }
}
