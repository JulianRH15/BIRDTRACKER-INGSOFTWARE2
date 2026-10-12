package co.edu.unbosque.BirdTracker.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import co.edu.unbosque.BirdTracker.model.Avistamiento;
import co.edu.unbosque.BirdTracker.repository.AvistamientoRepository;

@Service
@Transactional
public class AvistamientoService {

    private final AvistamientoRepository repository;

    public AvistamientoService(AvistamientoRepository repository) {
        this.repository = repository;
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

    public Avistamiento crear(Avistamiento avistamiento) {
        avistamiento.setId(null);
        return repository.save(avistamiento);
    }

    public Avistamiento actualizar(Long id, Avistamiento datos) {
        Avistamiento existente = buscar(id);
        existente.setEspecie(datos.getEspecie());
        existente.setLugar(datos.getLugar());
        existente.setFecha(datos.getFecha());
        existente.setObservador(datos.getObservador());
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
