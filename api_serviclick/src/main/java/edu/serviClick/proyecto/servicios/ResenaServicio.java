package edu.serviClick.proyecto.servicios;

import edu.serviClick.proyecto.entidades.Resena;
import edu.serviClick.proyecto.repositorios.ResenaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ResenaServicio {

    private static final Logger logger = LoggerFactory.getLogger(ResenaServicio.class);

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    public List<Resena> obtenerResenasPorServicio(Long servicioId) {
        return resenaRepositorio.findByServicioId(servicioId);
    }

    public Resena guardarResena(Resena resena) {
        boolean esNueva = (resena.getId() == null);
        Resena guardada = resenaRepositorio.save(resena);

        if (esNueva) {
            logger.info("Nueva reseña creada (ID: {}) para Servicio ID: {} por Usuario: {}",
                    guardada.getId(),
                    (resena.getServicio() != null ? resena.getServicio().getId() : "N/A"),
                    (resena.getUsuario() != null ? resena.getUsuario().getCorreo() : "Anónimo"));
        }
        return guardada;
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
        logger.info("Eliminando reseña con ID: {}", id);
        resenaRepositorio.deleteById(id);
    }
}
