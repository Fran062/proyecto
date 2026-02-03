package edu.serviClick.proyecto.servicios;

import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.repositorios.ModulosPlanRepositorio;
import edu.serviClick.proyecto.repositorios.SuscripcionesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuscripcionServicio {

    @Autowired
    private SuscripcionesRepositorio suscripcionRepositorio;

    @Autowired
    private ModulosPlanRepositorio moduloRepositorio;

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
        edu.serviClick.proyecto.entidades.Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Suscripcion suscripcion = suscripcionRepositorio.findByUsuarioId(usuarioId);

        if (suscripcion == null) {
            suscripcion = new Suscripcion();
            suscripcion.setUsuario(usuario);
        }

        suscripcion.setNombrePlan(nombrePlan);
        suscripcion.setPrecioTotalMensual(precio);
        suscripcion.setFechaInicio(new java.util.Date());
        suscripcion.setActiva(true);
        // Limpiamos módulos antiguos si los hubiera, ya que ahora usamos modo Plan
        suscripcion.setModulosContratados(null);

        return suscripcionRepositorio.save(suscripcion);
    }
}