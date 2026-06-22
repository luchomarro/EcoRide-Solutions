package com.ecoride.app.services;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.repositories.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicycleRepository;

    // Cumple con la Semana 6: Control de transacciones con @Transactional
    @Transactional(readOnly = true)
    public List<Bicicleta> listarTodas() {
        return bicycleRepository.findAll();
    }

    @Transactional
    public Bicicleta guardar(Bicicleta bicicleta) {
        return bicycleRepository.save(bicicleta);
    }
}