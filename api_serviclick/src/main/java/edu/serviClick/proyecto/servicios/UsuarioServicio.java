package edu.serviClick.proyecto.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.dto.LoginDTO;
import edu.serviClick.proyecto.entidades.Usuario;
import edu.serviClick.proyecto.repositorios.UsuariosRepositorio;

@Service
public class UsuarioServicio {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @Autowired
    private BCryptPasswordEncoder encriptadorContrasena;

    public UsuarioServicio(UsuariosRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public List<Usuario> buscarTodosLosUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {

        String contrasenaTextoPlano = usuario.getPassword();
        String contrasenaHasheada = encriptadorContrasena.encode(contrasenaTextoPlano);
        usuario.setPassword(contrasenaHasheada);

        return usuarioRepositorio.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorNombre(String nombre) {
        return usuarioRepositorio.findByNombreCompletoIgnoreCase(nombre);
    }

    public Usuario buscarPorId(Long id) {
        // Buscamos en la BBDD
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);

        // Si existe, lo devolvemos. Si no, devolvemos null.
        return usuario.orElse(null);
    }

    public List<Usuario> buscarUsuariosFlexible(String termino) {
        return usuarioRepositorio.findByNombreCompletoContainingIgnoreCase(termino);
    }

    public Optional<Usuario> buscarUsuarioPorCorreo(String correo) {
        return usuarioRepositorio.findByCorreo(correo);
    }

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
            return Optional.of(usuario);
        } else {
            return Optional.empty();
        }
    }

    public Usuario actualizarUsuario(Long id, Usuario usuarioActualizado) {
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
                // Verificar si ya está haseada (esto es un poco hacky, mejor sería un flag en
                // el DTO)
                // Asumimos que si viene del frontend en texto plano, no empieza por $2a$
                // Pero como el DTO del frontend es UsuarioDTO y aquí llega Usuario entity,
                // asumiremos siempre que si viene algo es para cambiarla
                String nuevaPass = encriptadorContrasena.encode(usuarioActualizado.getPassword());
                usuarioExistente.setPassword(nuevaPass);
            }

            return usuarioRepositorio.save(usuarioExistente);
        } else {
            throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
    }

    @Autowired
    private EmailServicio emailServicio;

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

        // Trim just in case
        String dbCode = usuario.getCodigoRecuperacion().trim();
        String inputCode = codigo != null ? codigo.trim() : "";

        boolean match = dbCode.equals(inputCode);
        if (!match)
            System.out.println("DEBUG: Códigos no coinciden.");

        return match;
    }

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
}
