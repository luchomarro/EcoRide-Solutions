package com.ecoride.app.controller; // <-- Debe estar en singular igual que tu carpeta

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.services.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*; // <-- Mapea las anotaciones @PostMapping y @GetMapping
import java.util.List;

@RestController // <-- Verifica que use RestController y NO @Controller convencional
@RequestMapping("/api/bicicletas")
@CrossOrigin(origins = "*")
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