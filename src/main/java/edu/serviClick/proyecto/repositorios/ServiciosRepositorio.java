package edu.serviClick.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.Servicio;

@Repository
public interface ServiciosRepositorio extends JpaRepository<Servicio, Long> {

}
