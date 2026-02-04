package edu.serviClick.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.Contratacion;

@Repository
public interface ContratacionesRepositorio extends JpaRepository<Contratacion, Long> {
    long countByServicioId(Long servicioId);
}
