package edu.serviClick.proyecto.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
    public Suscripcion crearSuscripcion(@RequestBody Suscripcion suscripcion) {
        return suscripcionServicio.guardarSuscripcion(suscripcion);
    }

}