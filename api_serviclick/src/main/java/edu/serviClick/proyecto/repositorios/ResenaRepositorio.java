package edu.serviClick.proyecto.repositorios;

import edu.serviClick.proyecto.entidades.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResenaRepositorio extends JpaRepository<Resena, Long> {
    List<Resena> findByServicioId(Long servicioId);
}
