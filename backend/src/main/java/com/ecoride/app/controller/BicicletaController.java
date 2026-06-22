package com.ecoride.app.controller;

import com.ecoride.app.model.Bicicleta;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/bicicletas")
public class BicicletaController {

    // 1. Muestra la vista del formulario de registro (GET)
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        // Pasamos un objeto vacío que el formulario llenará con sus campos
        model.addAttribute("bicicleta", new Bicicleta());
        return "formulario-bicicleta";
    }

    // 2. Procesa los datos enviados y redirige a la confirmación (POST)
    @PostMapping("/guardar")
    public String registrarBicicleta(@ModelAttribute("bicicleta") Bicicleta bicicleta, Model model) {
        // En una etapa posterior aquí se llamaría al service/repository para persistir en MySQL
        
        // Enviamos el objeto capturado hacia la vista de confirmación
        model.addAttribute("bicicletaRegistrada", bicicleta);
        return "confirmacion-bicicleta";
    }
}