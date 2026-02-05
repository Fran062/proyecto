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

    /**
     * Obtiene una lista con todos los usuarios registrados.
     * @return Lista de usuarios.
     */
    @GetMapping
    public List<Usuario> obtenerTodosLosUsuarios() {

        return usuarioServicio.buscarTodosLosUsuarios();
    }

    /**
     * Crea un nuevo usuario en el sistema.
     * @param usuario Objeto Usuario a crear.
     * @return Usuario creado y guardado.
     */
    @PostMapping
    public Usuario crearUsuario(@RequestBody Usuario usuario) {
        return usuarioServicio.guardarUsuario(usuario);
    }

    /**
     * Busca un usuario por su nombre de usuario/completo exacto.
     * @param nombre Nombre del usuario.
     * @return ResponseEntity con el usuario si existe, o 404 si no.
     */
    @GetMapping("/buscarNombreCompleto/{nombre}")
    public ResponseEntity<Usuario> obtenerUsuarioPorNombreUsuario(@PathVariable String nombre) {

        Optional<Usuario> usuario = usuarioServicio.buscarUsuarioPorNombre(nombre);

        // Si el usuario existe, devuelve 200 OK; si no, devuelve 404 Not Found.
        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Realiza una búsqueda flexible de usuarios que contengan el término dado en su nombre.
     * @param termino Término de búsqueda.
     * @return Lista de usuarios coincidentes o 204 No Content si está vacía.
     */
    @GetMapping("/buscar/{termino}")
    public ResponseEntity<List<Usuario>> buscarPorNombreFlexible(@PathVariable String termino) {

        List<Usuario> resultados = usuarioServicio.buscarUsuariosFlexible(termino);

        if (resultados.isEmpty()) {
            return ResponseEntity.noContent().build(); // Devuelve 204 si no encuentra a nadie
        }

        return ResponseEntity.ok(resultados); // Devuelve 200 y la lista de usuarios
    }

    // Endpoint simple: Dame el correo, te doy el usuario (con su hash de
    // contraseña)
    /**
     * Busca un usuario dado su correo electrónico.
     * @param correo Correo electrónico del usuario.
     * @return ResponseEntity con el usuario si existe, o 404 si no.
     */
    @PostMapping("/buscar-por-correo")
    public ResponseEntity<Usuario> obtenerUsuarioPorCorreo(@RequestBody String correo) {
        // Nota: @RequestBody String a veces trae comillas extra, mejor usar un objeto
        // simple o limpiar el string
        // Para simplificar, usaremos un DTO wrapper pequeño o asumimos que envías un
        // objeto JSON

        Optional<Usuario> usuario = usuarioServicio.buscarUsuarioPorCorreo(correo);

        return usuario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Solicita el inicio del proceso de recuperación de contraseña enviando un código al correo.
     * @param correo Correo electrónico del usuario.
     * @return 200 OK si se procesó (incluso si el correo no existe por seguridad), o 400 en error.
     */
    @PostMapping("/recuperar-password/solicitar")
    public ResponseEntity<Void> solicitarRecuperacion(@RequestBody String correo) {
        try {
            usuarioServicio.solicitarRecuperacionContrasena(correo);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Verifica si el código de recuperación proporcionado es válido para el correo.
     * @param payload Mapa con "correo" y "codigo".
     * @return true si es válido, false en caso contrario.
     */
    @PostMapping("/recuperar-password/verificar")
    public ResponseEntity<Boolean> verificarCodigo(@RequestBody java.util.Map<String, String> payload) {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        boolean valido = usuarioServicio.verificarCodigoRecuperacion(correo, codigo);
        return ResponseEntity.ok(valido);
    }

    /**
     * Cambia la contraseña del usuario utilizando un código de recuperación válido.
     * @param payload Mapa con "correo", "codigo" y "nuevaPassword".
     * @return 200 OK si se cambió con éxito, 400 Bad Request si falla.
     */
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

    /**
     * Actualiza los datos de un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param usuarioActualizado Objeto con los nuevos datos.
     * @return Usuario actualizado.
     */
    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioActualizado) {
        try {
            Usuario usuario = usuarioServicio.actualizarUsuario(id, usuarioActualizado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Confirma la cuenta de usuario mediante un token de verificación.
     * @param token Token de confirmación.
     * @return 200 OK si se confirmó, 400 Bad Request si falló.
     */
    @PostMapping("/confirmar")
    public ResponseEntity<Void> confirmarCuenta(@RequestBody String token) {
        if (usuarioServicio.confirmarCuenta(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Elimina un usuario del sistema por su ID.
     * @param id ID del usuario a eliminar.
     * @return 200 OK si se eliminó, 400 Bad Request si hubo error (ej. es el último admin).
     */
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
