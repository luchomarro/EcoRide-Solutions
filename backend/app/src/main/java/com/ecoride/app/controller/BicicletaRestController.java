package com.ecoride.app.controller;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.model.EstadoBicicleta;
import com.ecoride.app.services.BicicletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 * SEMANA 5 + 6 + 7 — REST Controller completo con documentación Swagger
 *
 * CRUD:
 *   GET    /api/bicicletas                     → listar todas (sin paginar)
 *   GET    /api/bicicletas/paginado?page=0&size=5&sort=nombre  → PAGINACIÓN
 *   GET    /api/bicicletas/{id}                → buscar por ID
 *   GET    /api/bicicletas/estado/{estado}     → JPQL named param
 *   POST   /api/bicicletas                     → crear (requiere JWT)
 *   PUT    /api/bicicletas/{id}                → actualizar (requiere JWT)
 *   DELETE /api/bicicletas/{id}                → eliminar (requiere JWT)
 *
 * Documentación interactiva disponible en /swagger-ui.html
 */
@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*")
@Tag(name = "Bicicletas", description = "Gestión del inventario de bicicletas de EcoRide Solutions")
public class BicicletaRestController {

    @Autowired
    private BicicletaService bicicletaService;

    // ── GET todos ─────────────────────────────────────────────────────────────

    @Operation(summary = "Listar todas las bicicletas",
            description = "Devuelve el inventario completo sin paginar. Acceso público.")
    @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    @GetMapping
    public List<Bicicleta> listar() {
        return bicicletaService.listarTodas();
    }

    // ── GET paginado ───────────────────────────────────────────────────────────

    @Operation(summary = "Listar bicicletas paginadas",
            description = "Devuelve el inventario en páginas, con ordenamiento configurable. Acceso público.")
    @ApiResponse(responseCode = "200", description = "Página obtenida correctamente")
    @GetMapping("/paginado")
    public Page<Bicicleta> listarPaginado(
            @Parameter(description = "Número de página, empieza en 0") @RequestParam(defaultValue = "0")  int page,
            @Parameter(description = "Cantidad de elementos por página") @RequestParam(defaultValue = "5")  int size,
            @Parameter(description = "Campo por el cual ordenar") @RequestParam(defaultValue = "id") String sort) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sort));
        return bicicletaService.listarPaginado(pageable);
    }

    // ── GET por ID ────────────────────────────────────────────────────────────

    @Operation(summary = "Buscar bicicleta por ID", description = "Acceso público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bicicleta encontrada"),
        @ApiResponse(responseCode = "404", description = "No existe una bicicleta con ese ID", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Bicicleta> buscarPorId(@PathVariable Long id) {
        return bicicletaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── GET por estado — JPQL named param ─────────────────────────────────────

    @Operation(summary = "Buscar bicicletas por estado",
            description = "Usa una consulta JPQL con parámetro nombrado. "
                    + "Valores válidos: DISPONIBLE, EN_USO, MANTENIMIENTO. Acceso público.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Resultado obtenido correctamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido", content = @Content)
    })
    @GetMapping("/estado/{estado}")
    public ResponseEntity<?> buscarPorEstado(
            @Parameter(description = "DISPONIBLE, EN_USO o MANTENIMIENTO") @PathVariable String estado) {
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

    @Operation(summary = "Registrar una nueva bicicleta",
            description = "Requiere autenticación JWT (rol ADMIN u OPERADOR).",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Bicicleta creada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Bicicleta> crear(@Valid @RequestBody Bicicleta bicicleta) {
        Bicicleta nueva = bicicletaService.guardar(bicicleta);
        return new ResponseEntity<>(nueva, HttpStatus.CREATED);
    }

    // ── PUT actualizar ────────────────────────────────────────────────────────

    @Operation(summary = "Actualizar una bicicleta existente",
            description = "Requiere autenticación JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Bicicleta actualizada correctamente"),
        @ApiResponse(responseCode = "404", description = "No existe una bicicleta con ese ID", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Bicicleta> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody Bicicleta datos) {

        return bicicletaService.actualizar(id, datos)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── DELETE eliminar ───────────────────────────────────────────────────────

    @Operation(summary = "Eliminar una bicicleta",
            description = "Requiere autenticación JWT.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Bicicleta eliminada correctamente"),
        @ApiResponse(responseCode = "404", description = "No existe una bicicleta con ese ID", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token ausente o inválido", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (bicicletaService.eliminar(id)) {
            return ResponseEntity.noContent().build();   // 204
        }
        return ResponseEntity.notFound().build();        // 404
    }
}
