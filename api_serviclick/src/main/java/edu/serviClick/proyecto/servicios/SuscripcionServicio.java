package edu.serviClick.proyecto.servicios;

import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.repositorios.SuscripcionesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SuscripcionServicio {

    private static final Logger logger = LoggerFactory.getLogger(SuscripcionServicio.class);

    @Autowired
    private SuscripcionesRepositorio suscripcionRepositorio;

    @Autowired
    private edu.serviClick.proyecto.repositorios.UsuariosRepositorio usuarioRepositorio;

    public List<Suscripcion> buscarTodasLasSuscripciones() {
        return suscripcionRepositorio.findAll();
    }

    public Suscripcion guardarSuscripcion(Suscripcion suscripcion) {
        return suscripcionRepositorio.save(suscripcion);
    }

    public Suscripcion crearSuscripcion(Suscripcion suscripcion) {
        // La API solo valida integridad de datos básicos y guarda
        return suscripcionRepositorio.save(suscripcion);
    }

    // Método para obtener suscripción por usuario
    public Suscripcion obtenerPorUsuario(Long usuarioId) {
        return suscripcionRepositorio.findByUsuarioId(usuarioId);
    }

    public Suscripcion contratarPlan(Long usuarioId, String nombrePlan, Double precio) {
        logger.info("Usuario ID: {} iniciando contratación/cambio de plan a: {}", usuarioId, nombrePlan);

        edu.serviClick.proyecto.entidades.Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> {
                    logger.error("Error al contratar plan: Usuario no encontrado con ID {}", usuarioId);
                    return new RuntimeException("Usuario no encontrado");
                });

        Suscripcion suscripcion = suscripcionRepositorio.findByUsuarioId(usuarioId);

        String accion = "Nueva contratación";
        if (suscripcion == null) {
            suscripcion = new Suscripcion();
            suscripcion.setUsuario(usuario);
        } else {
            accion = "Cambio de plan";
            logger.info("Usuario ID: {} cambiando plan actual ({}) a {}", usuarioId, suscripcion.getNombrePlan(),
                    nombrePlan);
        }

        suscripcion.setNombrePlan(nombrePlan);
        suscripcion.setPrecioTotalMensual(precio);
        suscripcion.setFechaInicio(new java.util.Date());
        suscripcion.setActiva(true);

        Suscripcion guardada = suscripcionRepositorio.save(suscripcion);
        logger.info("Éxito: {} completada para usuario ID: {}. Plan: {}", accion, usuarioId, nombrePlan);
        return guardada;
    }
}