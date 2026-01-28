package edu.backend_frontend_serviclick.controladores;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.dto.LoginRespuestaDTO;
import edu.backend_frontend_serviclick.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "index"; 
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginDTO datos, Model model) {
        
        System.out.println("Intentando login...");
        LoginRespuestaDTO respuesta = usuarioServicio.realizarLogin(datos);

        if (respuesta != null && respuesta.getToken() != null) {
            return "redirect:/pantallaInicio"; 
        } else {
            model.addAttribute("error", "Usuario o contraseña incorrectos");
            model.addAttribute("loginDTO", datos); 
            return "index"; 
        }
    }
    
    @GetMapping("/pantallaInicio")
    public String home() {
        return "pantallaInicio"; 
    }
}