package com.ecoride.app.controller;

import com.ecoride.app.model.Bicicleta;
import com.ecoride.app.model.EstadoBicicleta;
import com.ecoride.app.services.BicicletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/bicicletas")
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("bicicleta", new Bicicleta());
        model.addAttribute("estados", EstadoBicicleta.values());
        return "formulario-bicicleta";
    }

    @PostMapping("/guardar")
    public String registrarBicicleta(@ModelAttribute("bicicleta") Bicicleta bicicleta, Model model) {
        Bicicleta guardada = bicicletaService.guardar(bicicleta);
        model.addAttribute("bicicletaRegistrada", guardada);
        return "confirmacion-bicicleta";
    }

    @GetMapping("/lista")
    public String listar(Model model) {
        model.addAttribute("bicicletas", bicicletaService.listarTodas());
        return "lista-bicicletas";
    }
}