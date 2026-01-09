package edu.serviClick.proyecto.servicios;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
        return usuarioRepositorio.save(usuario);
    }

    public Optional<Usuario> buscarUsuarioPorNombre(String nombre) {
        return usuarioRepositorio.findByNombreCompletoIgnoreCase(nombre);
    }

    public List<Usuario> buscarUsuariosFlexible(String termino) {
    return usuarioRepositorio.findByNombreCompletoContainingIgnoreCase(termino);
}
}
