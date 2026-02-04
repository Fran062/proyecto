package edu.backend_frontend_serviclick.controladores;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.servicios.UsuarioServicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UsuarioControlador {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private edu.backend_frontend_serviclick.ApiCliente.ApiCliente apiCliente;

    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(@ModelAttribute LoginDTO datos, Model model, jakarta.servlet.http.HttpSession session) {

        logger.info("Intento de login para usuario: {}", datos.getCorreo());

        // Primero verificar si el usuario existe
        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioExistente = apiCliente
                .buscarUsuarioPorCorreo(datos.getCorreo());

        if (usuarioExistente != null && !Boolean.TRUE.equals(usuarioExistente.getHabilitado())) {
            logger.warn("Login fallido: Cuenta no activada para {}", datos.getCorreo());
            // Usuario existe pero cuenta no activada
            model.addAttribute("error",
                    "Tu cuenta no está activada. Por favor, revisa tu correo electrónico y haz clic en el enlace de activación.");
            model.addAttribute("loginDTO", datos);
            return "login";
        }

        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioLogueado = usuarioServicio.realizarLogin(datos);

        if (usuarioLogueado != null) {
            logger.info("Login EXITOSO para usuario: {} (ID: {}, Rol: {})",
                    usuarioLogueado.getCorreo(), usuarioLogueado.getId(), usuarioLogueado.getRol());
            session.setAttribute("usuarioLogueado", usuarioLogueado);
            return "redirect:/"; // Exito -> Inicio
        } else {
            logger.warn("Login fallido: Credenciales incorrectas para {}", datos.getCorreo());
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

        // Verificar si el correo ya existe
        try {
            edu.backend_frontend_serviclick.dto.UsuarioDTO existente = apiCliente
                    .buscarUsuarioPorCorreo(usuarioDTO.getCorreo());
            if (existente != null) {
                model.addAttribute("error",
                        "El correo electrónico ya está registrado. Por favor, utiliza otro o inicia sesión.");
                model.addAttribute("usuarioDTO", usuarioDTO);
                return "registro";
            }
        } catch (Exception e) {
            // Ignorar errores de conexión y dejar que registrarUsuario maneje la lógica o
            // falle
        }

        edu.backend_frontend_serviclick.dto.UsuarioDTO nuevoUsuario = usuarioServicio.registrarUsuario(usuarioDTO);

        if (nuevoUsuario != null) {
            return "redirect:/login"; // Exito -> Login
        } else {
            model.addAttribute("error", "Error al registrar el usuario. Comprueba los datos e inténtalo de nuevo.");
            model.addAttribute("usuarioDTO", usuarioDTO);
            return "registro"; // Fallo
        }
    }

    @GetMapping("/perfil/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {
        try {

            edu.backend_frontend_serviclick.dto.UsuarioDTO usuario = usuarioServicio.buscarPorId(id);

            if (usuario != null) {
                model.addAttribute("usuario", usuario);
                return "perfil";
            } else {
                return "error-404";
            }
        } catch (Exception e) {
            return "error-404";
        }
    }
}