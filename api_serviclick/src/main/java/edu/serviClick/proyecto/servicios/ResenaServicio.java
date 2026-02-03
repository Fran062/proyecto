package edu.serviClick.proyecto.servicios;

import edu.serviClick.proyecto.entidades.Resena;
import edu.serviClick.proyecto.repositorios.ResenaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResenaServicio {

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    public List<Resena> obtenerResenasPorServicio(Long servicioId) {
        return resenaRepositorio.findByServicioId(servicioId);
    }

    public Resena guardarResena(Resena resena) {
        return resenaRepositorio.save(resena);
    }

    public Double calcularPromedioCalificacion(Long servicioId) {
        List<Resena> resenas = resenaRepositorio.findByServicioId(servicioId);
        if (resenas.isEmpty()) {
            return 0.0;
        }
        double suma = 0;
        for (Resena r : resenas) {
            suma += r.getCalificacion();
        }
        return suma / resenas.size();
    }

    public void eliminarResena(Long id) {
        resenaRepositorio.deleteById(id);
    }
}
