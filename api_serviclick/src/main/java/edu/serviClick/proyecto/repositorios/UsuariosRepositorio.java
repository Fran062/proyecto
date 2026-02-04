package edu.serviClick.proyecto.repositorios;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.Usuario;

@Repository
public interface UsuariosRepositorio extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNombreCompletoIgnoreCase(String nombre);

    List<Usuario> findByNombreCompletoContainingIgnoreCase(String termino);

    Optional<Usuario> findByCorreo(String correo);

    Optional<Usuario> findByTokenVerificacion(String token);

    long countByRol(edu.serviClick.proyecto.enums.Rol rol);

}
