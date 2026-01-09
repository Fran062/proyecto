package edu.serviClick.proyecto.controladores;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
