package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.Servicio;
import edu.serviClick.proyecto.repositorios.ResenaRepositorio;
import edu.serviClick.proyecto.repositorios.ServiciosRepositorio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ServiciosService {

    private static final Logger logger = LoggerFactory.getLogger(ServiciosService.class);

    @Autowired
    private ServiciosRepositorio serviciosRepositorio;

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    public ServiciosService(ServiciosRepositorio serviciosRepositorio) {
        this.serviciosRepositorio = serviciosRepositorio;
    }

    /**
     * Recupera todos los servicios y calcula su calificación promedio.
     * @return Lista de servicios con promedio.
     */
    public List<Servicio> buscarTodosLosServicios() {
        List<Servicio> servicios = serviciosRepositorio.findAll();
        for (Servicio s : servicios) {
            Double promedio = calcularPromedio(s.getId());
            s.setPromedio(promedio);
        }
        return servicios;
    }

    /**
     * Calcula el promedio de calificaciones de las reseñas de un servicio.
     * @param servicioId ID del servicio.
     * @return Promedio.
     */
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

    /**
     * Guarda un servicio (creación o actualización).
     * @param servicio Entidad servicio.
     * @return Servicio guardado.
     */
    public Servicio guardarServicio(Servicio servicio) {
        boolean esNuevo = (servicio.getId() == null);
        Servicio guardado = serviciosRepositorio.save(servicio);

        if (esNuevo) {
            logger.info("Nuevo servicio publicado: '{}' (ID: {}) por Usuario ID: {}",
                    guardado.getTitulo(), guardado.getId(), guardado.getProfesional().getId());
        } else {
            logger.info("Servicio actualizado: '{}' (ID: {})", guardado.getTitulo(), guardado.getId());
        }

        return guardado;
    }

    /**
     * Busca un servicio por ID.
     * @param id ID del servicio.
     * @return El servicio o null.
     */
    public Servicio buscarPorId(Long id) {
        return serviciosRepositorio.findById(id).orElse(null);
    }

    @Autowired
    private edu.serviClick.proyecto.repositorios.ContratacionesRepositorio contratacionesRepositorio;

    /**
     * Elimina un servicio si no tiene contrataciones activas.
     * @param id ID del servicio.
     */
    public void eliminarServicio(Long id) {
        // Verificar integridad referencial: Contrataciones
        long count = contratacionesRepositorio.countByServicioId(id);
        if (count > 0) {
            throw new RuntimeException("No se puede eliminar el servicio. Tiene " + count
                    + " contrataciones asociadas. Por seguridad, no se permite el borrado directo.");
        }
        serviciosRepositorio.deleteById(id);
    }
}
