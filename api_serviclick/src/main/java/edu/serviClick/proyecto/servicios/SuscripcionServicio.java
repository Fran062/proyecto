package edu.serviClick.proyecto.servicios;

import edu.serviClick.proyecto.entidades.ModuloPlan;
import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.repositorios.ModulosPlanRepositorio;
import edu.serviClick.proyecto.repositorios.SuscripcionesRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class SuscripcionServicio {

    @Autowired
    private SuscripcionesRepositorio suscripcionRepositorio;

    @Autowired
    private ModulosPlanRepositorio moduloRepositorio;

    public List<Suscripcion> buscarTodasLasSuscripciones() {
        return suscripcionRepositorio.findAll();
    }

    public Suscripcion guardarSuscripcion(Suscripcion suscripcion) {
        return suscripcionRepositorio.save(suscripcion);
    }

    public Suscripcion crearSuscripcion(Suscripcion suscripcion) throws Exception {
        
        //Validar si ya existe
        Long usuarioId = suscripcion.getUsuario().getId();
        Suscripcion existente = suscripcionRepositorio.findByUsuarioId(usuarioId);
        
        if (existente != null && existente.isActiva()) {
            throw new Exception("El usuario ya tiene una suscripción activa.");
        }

        //Calcular Precios
        double precioCalculado = 0;
        List<ModuloPlan> listaReales = new ArrayList<>();

        if (suscripcion.getModulosContratados() != null) {
            for (ModuloPlan m : suscripcion.getModulosContratados()) {
                Optional<ModuloPlan> modDb = moduloRepositorio.findById(m.getId());
                if (modDb.isPresent()) {
                    ModuloPlan real = modDb.get();
                    precioCalculado += real.getPrecioMensual();
                    listaReales.add(real);
                }
            }
        }

        suscripcion.setModulosContratados(listaReales);
        suscripcion.setPrecioTotalMensual(precioCalculado);
        suscripcion.setFechaInicio(new Date());
        suscripcion.setActiva(true);

        return suscripcionRepositorio.save(suscripcion);
    }
    
    // Método para obtener suscripción por usuario
    public Suscripcion obtenerPorUsuario(Long usuarioId) {
        return suscripcionRepositorio.findByUsuarioId(usuarioId);
    }
}