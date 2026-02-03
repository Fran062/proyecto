package edu.serviClick.proyecto.controladores;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.ModuloPlan;
import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.repositorios.ModulosPlanRepositorio;
import edu.serviClick.proyecto.servicios.SuscripcionServicio;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionControlador {

    @Autowired
    private SuscripcionServicio suscripcionServicio;

    @Autowired
    private ModulosPlanRepositorio modulosPlanRepositorio;

    @GetMapping
    public List<Suscripcion> obtenerTodasLasSuscripciones() {

        return suscripcionServicio.buscarTodasLasSuscripciones();
    }

    @PostMapping
    public ResponseEntity<Suscripcion> crearSuscripcion(@RequestBody Suscripcion suscripcion) {

        double precioCalculado = 0;
        List<ModuloPlan> listaModulosReales = new ArrayList<>();

        if (suscripcion.getModulosContratados() != null) {

            for (ModuloPlan m : suscripcion.getModulosContratados()) {
                Optional<ModuloPlan> moduloDb = modulosPlanRepositorio.findById(m.getId());

                if (moduloDb.isPresent()) {
                    ModuloPlan moduloReal = moduloDb.get();

                    precioCalculado += moduloReal.getPrecioMensual();

                    listaModulosReales.add(moduloReal);
                }
            }
        }

        suscripcion.setModulosContratados(listaModulosReales);

        suscripcion.setPrecioTotalMensual(precioCalculado);

        suscripcion.setFechaInicio(new Date());
        suscripcion.setActiva(true);

        Suscripcion nueva = suscripcionServicio.guardarSuscripcion(suscripcion);
        return ResponseEntity.ok(nueva);
    }

    @PostMapping("/contratar")
    public ResponseEntity<Suscripcion> contratarPlan(@RequestBody java.util.Map<String, Object> payload) {
        try {
            Long usuarioId = Long.valueOf(payload.get("usuarioId").toString());
            String nombrePlan = payload.get("nombrePlan").toString();
            Double precio = Double.valueOf(payload.get("precio").toString());

            Suscripcion s = suscripcionServicio.contratarPlan(usuarioId, nombrePlan, precio);
            return ResponseEntity.ok(s);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

}