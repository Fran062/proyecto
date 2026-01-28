package edu.serviClick.proyecto.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.dto.LoginDTO;
import edu.serviClick.proyecto.entidades.Usuario;
import edu.serviClick.proyecto.servicios.UsuarioServicio;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {

        return usuarioServicio.buscarTodosLosUsuarios();
    }

    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioServicio.guardarUsuario(usuario);
    }

    @GetMapping("/buscarNombreCompleto/{nombre}")
    public ResponseEntity<Usuario> obtenerUsuarioPorNombreUsuario(@PathVariable String nombre) {

        Optional<Usuario> usuario = usuarioServicio.buscarUsuarioPorNombre(nombre);

        // Si el usuario existe, devuelve 200 OK; si no, devuelve 404 Not Found.
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{termino}")
    public ResponseEntity<List<Usuario>> buscarPorNombreFlexible(@PathVariable String termino) {

        List<Usuario> resultados = usuarioServicio.buscarUsuariosFlexible(termino);

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no encuentra a nadie
        }

        return ResponseEntity.ok(resultados); // Devuelve 200 y la lista de usuarios
    }

    @GetMapping("/{id}")
    public String verPerfil(@PathVariable Long id, Model model) {

        // Llamamos al método del servicio
        Usuario usuario = usuarioServicio.buscarPorId(id);

        if (usuario != null) {
            model.addAttribute("usuario", usuario);
            return "perfil-usuario"; // Tu archivo HTML
        } else {
            return "error-404"; // O redirigir a inicio
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO datos) {

        Optional<Usuario> usuario = usuarioServicio.autenticarUsuario(datos);

        if (usuario.isPresent()) {
            // Retorno Exitoso (200 OK)
            return ResponseEntity.ok(usuario.get());
        } else {
            // Retorno de Fallo (401 Unauthorized o 403 Forbidden)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario o contraseña incorrectos.");
        }
    }
}
