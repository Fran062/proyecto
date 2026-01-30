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
        return "login"; 
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginDTO datos, Model model) {
        
        System.out.println("Procesando login para: " + datos.getCorreo());
        
        // Llamamos a la API
        LoginRespuestaDTO respuesta = usuarioServicio.realizarLogin(datos);

        if (respuesta != null && respuesta.getToken() != null) {
            // --- ÉXITO ---
            System.out.println("Login correcto. Token recibido.");
            // Redirigimos a la lista de planes (o pantallaInicio)
            return "redirect:/planes";
        } else {
            // --- ERROR ---
            System.out.println("Login fallido.");
            
            // 1. Añadimos el mensaje de error al modelo
            model.addAttribute("error", "Credenciales incorrectas. Verifica tu correo y contraseña.");
            
            // 2. Devolvemos el objeto loginDTO para que no tenga que escribir el correo de nuevo
            model.addAttribute("loginDTO", datos); 
            
            // 3. IMPORTANTE: Volvemos a cargar el HTML de "login", no el "index"
            return "login"; 
        }
    }
    
    @GetMapping("/planes")
    public String home() {
        return "lista-planes"; 
    }
}