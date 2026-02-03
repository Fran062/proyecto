package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.Servicio;
import edu.serviClick.proyecto.repositorios.ResenaRepositorio;
import edu.serviClick.proyecto.repositorios.ServiciosRepositorio;

@Service
public class ServiciosService {

    @Autowired
    private ServiciosRepositorio serviciosRepositorio;

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    public ServiciosService(ServiciosRepositorio serviciosRepositorio) {
        this.serviciosRepositorio = serviciosRepositorio;
    }

    public List<Servicio> buscarTodosLosServicios() {
        List<Servicio> servicios = serviciosRepositorio.findAll();
        for (Servicio s : servicios) {
            Double promedio = calcularPromedio(s.getId());
            s.setPromedio(promedio);
        }
        return servicios;
    }

    private Double calcularPromedio(Long servicioId) {
        java.util.List<edu.serviClick.proyecto.entidades.Resena> resenas = resenaRepositorio
                .findByServicioId(servicioId);
        if (resenas.isEmpty())
            return 0.0;
        double suma = 0;
        for (edu.serviClick.proyecto.entidades.Resena r : resenas) {
            suma += r.getCalificacion();
        }
        return suma / resenas.size();
    }

    public Servicio guardarServicio(Servicio servicio) {
        return serviciosRepositorio.save(servicio);
    }

}
