package com.ecoride.app.controller;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.services.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*") // Crucial para la arquitectura distribuida (CORS con Angular)
public class BicicletaRestController {

    @Autowired
    private BicicletaService bicycleService;

    @GetMapping
    public List<Bicicleta> listar() {
        return bicycleService.listarTodas();
    }

    @PostMapping
    public ResponseEntity<Bicicleta> crear(@RequestBody Bicicleta bicicleta) {
        Bicicleta nuevaBicicleta = bicycleService.guardar(bicicleta);
        return new ResponseEntity<>(nuevaBicicleta, HttpStatus.CREATED);
    }
}