package edu.serviClick.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.Suscripcion;

@Repository
public interface SuscripcionesRepositorio extends JpaRepository<Suscripcion, Long> {

    Suscripcion findByUsuarioId(Long usuarioId);

}
