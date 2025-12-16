package edu.serviClick.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.Usuario;

@Repository
public interface UsuariosRepositorio extends JpaRepository<Usuario, Long> {

}
