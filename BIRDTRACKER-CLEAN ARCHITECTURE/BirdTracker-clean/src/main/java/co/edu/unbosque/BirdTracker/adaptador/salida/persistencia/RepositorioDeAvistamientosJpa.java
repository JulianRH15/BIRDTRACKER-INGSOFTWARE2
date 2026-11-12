package co.edu.unbosque.BirdTracker.adaptador.salida.persistencia;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unbosque.BirdTracker.aplicacion.modelo.Avistamiento;
import co.edu.unbosque.BirdTracker.aplicacion.modelo.ResumenEspecie;
import co.edu.unbosque.BirdTracker.aplicacion.puerto.salida.RepositorioDeAvistamientos;

/**
 * Adaptador de salida: implementa el puerto RepositorioDeAvistamientos con JPA y H2.
 * Traduce entre el modelo de la aplicación y la fila de la base de datos.
 */
@Component
@Transactional
public class RepositorioDeAvistamientosJpa implements RepositorioDeAvistamientos {
    private final AvistamientoJpaRepository jpa;

    public RepositorioDeAvistamientosJpa(AvistamientoJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Avistamiento> listarTodos() {
        return jpa.findAll().stream().map(AvistamientoJpa::aModelo).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Avistamiento> buscarPorId(Long id) {
        return jpa.findById(id).map(AvistamientoJpa::aModelo);
    }

    @Override
    public Avistamiento guardar(Avistamiento avistamiento) {
        return jpa.save(AvistamientoJpa.desde(avistamiento)).aModelo();
    }

    @Override
    public void eliminar(Avistamiento avistamiento) {
        jpa.deleteById(avistamiento.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResumenEspecie> resumirPorEspecie() {
        return jpa.resumirPorEspecie().stream()
                .map(fila -> new ResumenEspecie(fila.getEspecie(), fila.getCantidad()))
                .toList();
    }
}
