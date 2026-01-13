package edu.serviClick.proyecto.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.serviClick.proyecto.entidades.ModuloPlan;

@Repository
public interface ModulosPlanRepositorio extends JpaRepository<ModuloPlan, Long> {

}