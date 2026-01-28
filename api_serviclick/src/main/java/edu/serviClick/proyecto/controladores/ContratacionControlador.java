package edu.serviClick.proyecto.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.Contratacion;
import edu.serviClick.proyecto.servicios.ContratacionServicio;

@RestController
@RequestMapping("/api/contrataciones")
public class ContratacionControlador {

    @Autowired
    private ContratacionServicio contratacionServicio;

    @GetMapping
    public List<Contratacion> obtenerTodasLasContrataciones() {

        return contratacionServicio.buscarTodasLasContrataciones();
    }

    @PostMapping
    public Contratacion crearContratacion(@RequestBody Contratacion contratacion) {
        return contratacionServicio.guardarContratacion(contratacion);
    }

}