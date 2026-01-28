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

    public Optional<Usuario> autenticarUsuario(LoginDTO datos) {
        Optional<Usuario> usuarioOpt = usuarioRepositorio.findByCorreo(datos.getCorreo());

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();

        boolean passwordMatch = encriptadorContrasena.matches(
                datos.getContraseña(),
                usuario.getPassword());

        if (passwordMatch) {
            return Optional.of(usuario);
        } else {
            return Optional.empty();
        }
    }
}
