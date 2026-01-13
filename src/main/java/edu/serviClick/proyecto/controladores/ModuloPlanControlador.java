package edu.serviClick.proyecto.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.ModuloPlan;
import edu.serviClick.proyecto.servicios.ModuloPlanServicio;

@RestController
@RequestMapping("/api/moduloPlan")
public class ModuloPlanControlador {
    @Autowired
    private ModuloPlanServicio moduloPlanServicio;

    @GetMapping
    public List<ModuloPlan> obtenerTodosLosModulosPlan() {

        return moduloPlanServicio.buscarTodosLosModulosPlan();
    }

    @PostMapping
    public ModuloPlan crearModuloPlan(@RequestBody ModuloPlan moduloPlan) {
        return moduloPlanServicio.guardarModuloPlan(moduloPlan);
    }

}