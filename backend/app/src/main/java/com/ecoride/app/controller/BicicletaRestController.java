package com.ecoride.app.controller;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.model.EstadoBicicleta;
import com.ecoride.app.services.BicicletaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * SEMANA 5 + 6 — REST Controller completo
 *
 * CRUD:
 *   GET    /api/bicicletas                     → listar todas (sin paginar)
 *   GET    /api/bicicletas/paginado?page=0&size=5&sort=nombre  → PAGINACIÓN (Sem 6)
 *   GET    /api/bicicletas/{id}                → buscar por ID
 *   GET    /api/bicicletas/estado/{estado}     → JPQL named param (Sem 6)
 *   POST   /api/bicicletas                     → crear (requiere JWT)
 *   PUT    /api/bicicletas/{id}                → actualizar (requiere JWT)
 *   DELETE /api/bicicletas/{id}                → eliminar (requiere JWT)
 */
@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*")
public class BicicletaRestController {

    @Autowired
    private BicicletaService bicicletaService;

    // ── GET todos ─────────────────────────────────────────────────────────────

    @GetMapping
    public List<Bicicleta> listar() {
        return bicicletaService.listarTodas();
    }

    // ── GET paginado (SEMANA 6) ───────────────────────────────────────────────
    // Uso: GET /api/bicicletas/paginado?page=0&size=5&sort=nombre,asc

    @GetMapping("/paginado")
    public Page<Bicicleta> listarPaginado(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "5")  int size,
            @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return bicicletaService.listarPaginado(pageable);
    }

    // ── GET por ID ────────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public ResponseEntity<Bicicleta> buscarPorId(@PathVariable Long id) {
        return bicicletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET por estado — JPQL named param (SEMANA 6) ─────────────────────────
    // Uso: GET /api/bicicletas/estado/DISPONIBLE

    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(@PathVariable String estado) {
        try {
            EstadoBicicleta estadoEnum = EstadoBicicleta.valueOf(estado.toUpperCase());
            List<Bicicleta> resultado = bicicletaService.buscarPorEstado(estadoEnum);
            return ResponseEntity.ok(resultado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Estado inválido. Valores permitidos: DISPONIBLE, EN_USO, MANTENIMIENTO");
        }
    }

    // ── POST crear ────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<Bicicleta> crear(@Valid @RequestBody Bicicleta bicicleta) {
        Bicicleta nueva = bicicletaService.guardar(bicicleta);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // ── PUT actualizar (SEMANA 5) ─────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Bicicleta datos) {

        return bicicletaService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE eliminar (SEMANA 5) ────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (bicicletaService.eliminar(id)) {
            return ResponseEntity.noContent().build();   // 204
        }
        return ResponseEntity.notFound().build();        // 404
    }
}
