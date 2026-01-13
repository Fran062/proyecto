package edu.serviClick.proyecto.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.Servicio;
import edu.serviClick.proyecto.servicios.ServiciosService;

@RestController
@RequestMapping("/api/servicios")
public class ServicioControlador {
    @Autowired
    private ServiciosService serviciosService;

    @GetMapping
    public List<Servicio> obtenerTodosLosServicios() {

        return serviciosService.buscarTodosLosServicios();
    }

    @PostMapping
    public Servicio crearServicio(@RequestBody Servicio servicio) {
        return serviciosService.guardarServicio(servicio);
    }

}