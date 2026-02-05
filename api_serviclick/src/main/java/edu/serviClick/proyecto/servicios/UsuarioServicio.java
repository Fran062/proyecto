package edu.serviClick.proyecto.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.dto.LoginDTO;
import edu.serviClick.proyecto.entidades.Usuario;
import edu.serviClick.proyecto.repositorios.UsuariosRepositorio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UsuarioServicio {

    private static final Logger logger = LoggerFactory.getLogger(UsuarioServicio.class);

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @Autowired
    private BCryptPasswordEncoder encriptadorContrasena;

    @Autowired
    private EmailServicio emailServicio;

    public UsuarioServicio(UsuariosRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    /**
     * Recupera todos los usuarios de la base de datos.
     * @return Lista de entidades Usuario.
     */
    public List<Usuario> buscarTodosLosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    /**
     * Registra un nuevo usuario, encriptando su contraseña y generando topken de validación.
     * @param usuario Datos del usuario a registrar.
     * @return El usuario guardado.
     */
    public Usuario guardarUsuario(Usuario usuario) {
        logger.info("Iniciando registro de nuevo usuario: {}", usuario.getCorreo());

        String contrasenaTextoPlano = usuario.getPassword();
        String contrasenaHasheada = encriptadorContrasena.encode(contrasenaTextoPlano);
        usuario.setPassword(contrasenaHasheada);

        usuario.setHabilitado(false);
        String token = java.util.UUID.randomUUID().toString();
        usuario.setTokenVerificacion(token);

        Usuario guardado = usuarioRepositorio.save(usuario);
        logger.info("Usuario registrado exitosamente en BD (Pendiente de activación): {}", guardado.getId());

        try {
            emailServicio.enviarConfirmacion(guardado.getCorreo(), token);
            logger.info("Correo de confirmación enviado a: {}", guardado.getCorreo());
        } catch (Exception e) {
            logger.error("Error enviando correo de confirmación a {}: {}", guardado.getCorreo(), e.getMessage());
        }
        return guardado;
    }

    /**
     * Busca un usuario por coincidencia exacta de nombre completo (ignore case).
     * @param nombre Nombre completo a buscar.
     * @return Optional con el usuario si existe.
     */
    public Optional<Usuario> buscarUsuarioPorNombre(String nombre) {
        return usuarioRepositorio.findByNombreCompletoIgnoreCase(nombre);
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario.
     * @return El usuario encontrado o null.
     */
    public Usuario buscarPorId(Long id) {
        // Buscamos en la BBDD
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);

        // Si existe, lo devolvemos. Si no, devolvemos null.
        return usuario.orElse(null);
    }

    /**
     * Busca usuarios cuyo nombre contenga el término proporcionado.
     * @param termino Término de búsqueda.
     * @return Lista de usuarios coincidentes.
     */
    public List<Usuario> buscarUsuariosFlexible(String termino) {
        return usuarioRepositorio.findByNombreCompletoContainingIgnoreCase(termino);
    }

    /**
     * Busca un usuario por su correo electrónico.
     * @param correo Correo del usuario.
     * @return Optional con el usuario si existe.
     */
    public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

    /**
     * Autentica a un usuario verificando correo y contraseña.
     * @param datos DTO con credenciales (correo y password).
     * @return Optional con el usuario si la autenticación es exitosa y está habilitado.
     */
    public Optional<Usuario> autenticarUsuario(LoginDTO datos) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(datos.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        boolean passwordMatch = encriptadorContrasena.matches(
                datos.getContrasena(),
                usuario.getPassword());

        if (passwordMatch) {
            if (!Boolean.TRUE.equals(usuario.getHabilitado())) {
                System.out.println("Usuario no habilitado. Debe confirmar su correo.");
                return Optional.empty();
            }
            return Optional.of(usuario);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Activa la cuenta de un usuario mediante el token de verificación.
     * @param token Token enviado por correo.
     * @return true si la activación fue exitosa.
     */
    public boolean confirmarCuenta(String token) {
        Optional<Usuario> userOpt = usuarioRepositorio.findByTokenVerificacion(token);
        if (userOpt.isPresent()) {
            Usuario usuario = userOpt.get();
            usuario.setHabilitado(true);
            usuario.setTokenVerificacion(null);
            usuarioRepositorio.save(usuario);
            return true;
        }
        return false;
    }

    /**
     * Actualiza la información de perfil de un usuario.
     * @param id ID del usuario.
     * @param usuarioActualizado Objeto con los nuevos datos (nombre, telefono, password).
     * @return El usuario actualizado.
     */
    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
        logger.info("Intento de actualización de perfil para usuario ID: {}", id);

        Optional<Usuario> usuarioExistenteOpt = usuarioRepositorio.findById(id);

        if (usuarioExistenteOpt.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOpt.get();

            // Actualizar campos permitidos
            if (usuarioActualizado.getNombreCompleto() != null) {
                usuarioExistente.setNombreCompleto(usuarioActualizado.getNombreCompleto());
            }
            if (usuarioActualizado.getTelefono() != null) {
                usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
            }

            // Si la contraseña viene rellena, la actualizamos hasheada
            if (usuarioActualizado.getPassword() != null && !usuarioActualizado.getPassword().isEmpty()) {
                String nuevaPass = encriptadorContrasena.encode(usuarioActualizado.getPassword());
                usuarioExistente.setPassword(nuevaPass);
            }

            Usuario actualizado = usuarioRepositorio.save(usuarioExistente);
            logger.info("Perfil actualizado exitosamente para usuario ID: {}", id);
            return actualizado;
        } else {
            logger.error("Error al actualizar perfil: Usuario no encontrado con ID: {}", id);
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    /**
     * Inicia el flujo de recuperación de contraseña generando un código temporal.
     * @param correo Correo del usuario.
     */
    public void solicitarRecuperacionContrasena(String correo) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con correo: " + correo);
        }
        Usuario usuario = usuarioOpt.get();
        // Generar código de 4 dígitos
        String codigo = String.valueOf((int) (Math.random() * 9000) + 1000);
        usuario.setCodigoRecuperacion(codigo);
        usuario.setFechaExpiracionCodigo(java.time.LocalDateTime.now().plusMinutes(15));
        usuarioRepositorio.save(usuario);

        emailServicio.enviarCodigoRecuperacion(correo, codigo);
    }

    /**
     * Verifica la validez de un código de recuperación.
     * @param correo Correo del usuario.
     * @param codigo Código de 4 dígitos.
     * @return true si es válido y no ha expirado.
     */
    public boolean verificarCodigoRecuperacion(String correo, String codigo) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(correo);
        if (usuarioOpt.isEmpty()) {
            System.out.println("DEBUG: Usuario no encontrado para correo: " + correo);
            return false;
        }
        Usuario usuario = usuarioOpt.get();

        System.out.println(
                "DEBUG: Verificando código. DB Code: " + usuario.getCodigoRecuperacion() + ", Input Code: " + codigo);
        System.out.println("DEBUG: Fecha Expiración: " + usuario.getFechaExpiracionCodigo() + ", Ahora: "
                + java.time.LocalDateTime.now());

        if (usuario.getCodigoRecuperacion() == null || usuario.getFechaExpiracionCodigo() == null) {
            System.out.println("DEBUG: Código o fecha nulos.");
            return false;
        }
        if (usuario.getFechaExpiracionCodigo().isBefore(java.time.LocalDateTime.now())) {
            System.out.println("DEBUG: Código expirado.");
            return false;
        }

        String dbCode = usuario.getCodigoRecuperacion().trim();
        String inputCode = codigo != null ? codigo.trim() : "";

        boolean match = dbCode.equals(inputCode);
        if (!match)
            System.out.println("DEBUG: Códigos no coinciden.");

        return match;
    }

    /**
     * Establece una nueva contraseña tras validar el código de recuperación.
     * @param correo Correo del usuario.
     * @param codigo Código de verificación.
     * @param nuevaPassword Nueva contraseña en texto plano.
     */
    public void cambiarContrasenaConCodigo(String correo, String codigo, String nuevaPassword) {
        if (!verificarCodigoRecuperacion(correo, codigo)) {
            // Logs ya están en verificarCodigoRecuperacion
            throw new RuntimeException("Código inválido o expirado");
        }
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(correo);
        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            String passHasheada = encriptadorContrasena.encode(nuevaPassword);
            usuario.setPassword(passHasheada);
            // Limpiar código
            usuario.setCodigoRecuperacion(null);
            usuario.setFechaExpiracionCodigo(null);
            usuarioRepositorio.save(usuario);
        }
    }

    /**
     * Elimina un usuario por ID, con validaciones de seguridad (ej. no borrar último admin).
     * @param id ID del usuario.
     */
    public void eliminarUsuario(Long id) {
        Usuario usuario = usuarioRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (usuario.getRol() == edu.serviClick.proyecto.enums.Rol.ADMIN) {
            long adminCount = usuarioRepositorio.countByRol(edu.serviClick.proyecto.enums.Rol.ADMIN);
            if (adminCount <= 1) {
                throw new RuntimeException(
                        "Operación denegada: No se puede eliminar al único administrador del sistema.");
            }
        }

        usuarioRepositorio.deleteById(id);
        logger.info("Usuario con ID {} eliminado exitosamente.", id);
    }
}
