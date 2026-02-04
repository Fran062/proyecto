package edu.serviClick.proyecto.controladores;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.Suscripcion;
import edu.serviClick.proyecto.servicios.SuscripcionServicio;

@RestController
@RequestMapping("/api/suscripciones")
public class SuscripcionControlador {

    @Autowired
    private SuscripcionServicio suscripcionServicio;

    @GetMapping
    public List<Suscripcion> obtenerTodasLasSuscripciones() {

        return suscripcionServicio.buscarTodasLasSuscripciones();
    }

    @PostMapping
    public ResponseEntity<Suscripcion> crearSuscripcion(@RequestBody Suscripcion suscripcion) {

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