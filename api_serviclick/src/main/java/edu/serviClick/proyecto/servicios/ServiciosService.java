package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.Servicio;
import edu.serviClick.proyecto.repositorios.ServiciosRepositorio;

@Service
public class ServiciosService {

    @Autowired
    private ServiciosRepositorio serviciosRepositorio;

    public ServiciosService(ServiciosRepositorio serviciosRepositorio) {
        this.serviciosRepositorio = serviciosRepositorio;
    }


    public List<Servicio> buscarTodosLosServicios() {
        return serviciosRepositorio.findAll();
    }

    public Servicio guardarServicio(Servicio servicio) {
        return serviciosRepositorio.save(servicio);
    }

    
}
