package co.edu.unbosque.BirdTracker.aplicacion.servicio;

import java.util.List;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.AvistamientoNoEncontradoException;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ResumenEspecie;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ValidadorDeAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.ActualizarAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.ConsultarAvistamientos;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.CrearAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.DatosDeAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.entrada.EliminarAvistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.RepositorioDeAvistamientos;

/**
 * Implementa los puertos de entrada. Es Java puro: no conoce Spring, ni HTTP, ni JPA.
 * Todo lo que necesita de afuera lo pide por los puertos de salida.
 */
public class AvistamientoService
        implements CrearAvistamiento, ConsultarAvistamientos, ActualizarAvistamiento, EliminarAvistamiento {
    private final RepositorioDeAvistamientos repository;
    private final ValidadorDeAvistamiento validador;

    public AvistamientoService(RepositorioDeAvistamientos repository, ValidadorDeAvistamiento validador) {
        this.repository = repository;
        this.validador = validador;
    }

    @Override
    public List<Avistamiento> listar() {
        return repository.listarTodos();
    }

    @Override
    public Avistamiento buscar(Long id) {
        return repository.buscarPorId(id).orElseThrow(() -> new AvistamientoNoEncontradoException(id));
    }

    @Override
    public Avistamiento crear(DatosDeAvistamiento datos) {
        validador.validar(datos);
        return repository.guardar(normalizar(null, datos));
    }

    @Override
    public Avistamiento actualizar(Long id, DatosDeAvistamiento datos) {
        validador.validar(datos);
        Avistamiento existente = buscar(id);
        return repository.guardar(normalizar(existente.getId(), datos));
    }

    @Override
    public void eliminar(Long id) {
        Avistamiento existente = buscar(id);
        repository.eliminar(existente);
    }

    @Override
    public List<ResumenEspecie> resumen() {
        return repository.resumirPorEspecie();
    }

    private static Avistamiento normalizar(Long id, DatosDeAvistamiento datos) {
        return new Avistamiento(id, datos.especie().strip(), datos.lugar().strip(),
                ValidadorDeAvistamiento.convertirFecha(datos.fecha().strip()), datos.observador().strip());
    }
}
