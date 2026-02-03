package edu.backend_frontend_serviclick.controladores;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.dto.LoginRespuestaDTO;
import edu.backend_frontend_serviclick.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginDTO datos, Model model, jakarta.servlet.http.HttpSession session) {

        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioLogueado = usuarioServicio.realizarLogin(datos);

        if (usuarioLogueado != null) {
            session.setAttribute("usuarioLogueado", usuarioLogueado);
            return "redirect:/"; // Exito -> Inicio
        } else {
            model.addAttribute("error", "Credenciales incorrectas");
            model.addAttribute("loginDTO", datos);
            return "login"; // Fallo
        }
    }

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        model.addAttribute("usuarioDTO", new edu.backend_frontend_serviclick.dto.UsuarioDTO());
        return "registro";
    }

    @PostMapping("/registro")
    public String procesarRegistro(@ModelAttribute edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioDTO,
            Model model) {
        edu.backend_frontend_serviclick.dto.UsuarioDTO nuevoUsuario = usuarioServicio.registrarUsuario(usuarioDTO);

        if (nuevoUsuario != null) {
            return "redirect:/login"; // Exito -> Login
        } else {
            model.addAttribute("error", "Error al registrar el usuario");
            model.addAttribute("usuarioDTO", usuarioDTO);
            return "registro"; // Fallo
        }
    }

    // El método home /planes se ha movido a PortalControlador generalmente, pero si
    // se accede por aquí:
    // Mejor lo quitamos para evitar duplicidad o lo dejamos si es específico de
    // usuario.
    // En el plan dijimos que PortalControlador lleva los planes.

    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        // Debemos buscar el usuario en la API
        // Necesitamos un método en UsuarioServicio que llame a la API
        try {
            // Asumimos que tendremos este método o usamos un DTO
            // Pero el servicio devuelve DTO o Entidad?
            // En el Web (este proyecto), usamos DTOs preferiblemente o la entidad duplicada
            // si existe.
            // Vamos a ver qué devuelve el servicio.
            // El servicio `realizarLogin` usaba `UsuarioDTO`.
            // Usaremos UsuarioDTO para el perfil también.

            edu.backend_frontend_serviclick.dto.UsuarioDTO usuario = usuarioServicio.buscarPorId(id);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                return "perfil-usuario"; // Asegurarse que el template se llame así
            } else {
                return "error-404"; // O redirigir
            }
        } catch (Exception e) {
            return "error-404";
        }
    }
}