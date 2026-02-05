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

    @PostMapping("/buscar-por-correo")
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestBody String correo) {

        Optional<Usuario> usuario = usuarioServicio.buscarUsuarioPorCorreo(correo);

        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/recuperar-password/solicitar")
    public ResponseEntity<Void> solicitarRecuperacion(@RequestBody String correo) {
        try {
            usuarioServicio.solicitarRecuperacionContrasena(correo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/recuperar-password/verificar")
    public ResponseEntity<Boolean> verificarCodigo(@RequestBody java.util.Map<String, String> payload) {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        boolean valido = usuarioServicio.verificarCodigoRecuperacion(correo, codigo);
        return ResponseEntity.ok(valido);
    }

    @PostMapping("/recuperar-password/cambiar")
    public ResponseEntity<Void> cambiarPassword(@RequestBody java.util.Map<String, String> payload) {
        try {
            String correo = payload.get("correo");
            String codigo = payload.get("codigo");
            String nuevaPassword = payload.get("nuevaPassword");

            System.out.println("DEBUG: Iniciando cambio de contraseña para " + correo);

            usuarioServicio.cambiarContrasenaConCodigo(correo, codigo, nuevaPassword);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.err.println("ERROR en cambiarPassword: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioServicio.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarCuenta(@RequestBody String token) {
        if (usuarioServicio.confirmarCuenta(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        try {
            usuarioServicio.eliminarUsuario(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
