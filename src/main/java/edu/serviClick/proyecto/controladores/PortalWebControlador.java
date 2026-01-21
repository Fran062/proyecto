package edu.serviClick.proyecto.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalWebControlador {

    // Cuando entras a http://localhost:8080/, muestra index.html
    @GetMapping("/")
    public String index() {
        return "index"; 
    }

    // 2. Para ver los planes
    @GetMapping("/web/planes")
    public String verPlanes() {
        return "lista-planes"; 
    }

    // 3. Para la pantalla de pago
    @GetMapping("/web/pago")
    public String irAPagar(@RequestParam String plan, 
                           @RequestParam String precio, 
                           Model model) {
        model.addAttribute("planSeleccionado", plan);
        model.addAttribute("precioSeleccionado", precio);
        return "vista-pago"; 
    }
}