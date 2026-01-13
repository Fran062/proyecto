package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.repositorios.SuscripcionesRepositorio;

@Service
public class SuscripcionServicio {

    @Autowired
    private SuscripcionesRepositorio suscripcionesRepositorio;

    public SuscripcionServicio(SuscripcionesRepositorio suscripcionesRepositorio) {
        this.suscripcionesRepositorio = suscripcionesRepositorio;
    }


    public List<Suscripcion> buscarTodasLasSuscripciones() {
        return suscripcionesRepositorio.findAll();
    }

    public Suscripcion guardarSuscripcion(Suscripcion suscripcion) {
        return suscripcionesRepositorio.save(suscripcion);
    }

    
}
