package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.Contratacion;
import edu.serviClick.proyecto.repositorios.ContratacionesRepositorio;

@Service
public class ContratacionServicio {

    @Autowired
    private ContratacionesRepositorio contratacionesRepositorio;

    public ContratacionServicio(ContratacionesRepositorio contratacionesRepositorio) {
        this.contratacionesRepositorio = contratacionesRepositorio;
    }

    public List<Contratacion> buscarTodasLasContrataciones() {
        return contratacionesRepositorio.findAll();
    }

    public Contratacion guardarContratacion(Contratacion contratacion) {
        return contratacionesRepositorio.save(contratacion);
    }

}
