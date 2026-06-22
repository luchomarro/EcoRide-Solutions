package com.ecoride.app.services;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.model.EstadoBicicleta;
import com.ecoride.app.repositories.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * SEMANA 6 — Service actualizado
 * - @Transactional en todas las operaciones
 * - Paginación con Page<Bicicleta> y Pageable
 * - Búsqueda por estado usando el JPQL del Repository
 */
@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicycleRepository;

    // ── Lectura ───────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<Bicicleta> listarTodas() {
        return bicycleRepository.findAll();
    }

    /** SEMANA 6: Paginación con Pageable */
    @Transactional(readOnly = true)
    public Page<Bicicleta> listarPaginado(Pageable pageable) {
        return bicycleRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<Bicicleta> buscarPorId(Long id) {
        return bicycleRepository.findById(id);
    }

    /** SEMANA 6: Usa el @Query JPQL con parámetro nombrado */
    @Transactional(readOnly = true)
    public List<Bicicleta> buscarPorEstado(EstadoBicicleta estado) {
        return bicycleRepository.buscarPorEstadoParametrizado(estado);
    }

    // ── Escritura ─────────────────────────────────────────────────────────────

    @Transactional
    public Bicicleta guardar(Bicicleta bicicleta) {
        return bicycleRepository.save(bicicleta);
    }

    /** SEMANA 5: update — busca la entidad, aplica cambios, guarda */
    @Transactional
    public Optional<Bicicleta> actualizar(Long id, Bicicleta datos) {
        return bicycleRepository.findById(id).map(existente -> {
            existente.setNombre(datos.getNombre());
            existente.setDescripcion(datos.getDescripcion());
            existente.setPrecioHora(datos.getPrecioHora());
            existente.setEstado(datos.getEstado());
            existente.setUrlImagen(datos.getUrlImagen());
            return bicycleRepository.save(existente);
        });
    }

    /** SEMANA 5: delete */
    @Transactional
    public boolean eliminar(Long id) {
        if (bicycleRepository.existsById(id)) {
            bicycleRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
