package edu.serviClick.proyecto.servicios;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.serviClick.proyecto.entidades.ModuloPlan;
import edu.serviClick.proyecto.repositorios.ModulosPlanRepositorio;

@Service
public class ModuloPlanServicio {

    @Autowired
    private ModulosPlanRepositorio modulosPlanRepositorio;

    public ModuloPlanServicio(ModulosPlanRepositorio modulosPlanRepositorio) {
        this.modulosPlanRepositorio = modulosPlanRepositorio;
    }


    public List<ModuloPlan> buscarTodosLosModulosPlan() {
        return modulosPlanRepositorio.findAll();
    }

    public ModuloPlan guardarModuloPlan(ModuloPlan moduloPlan) {
        return modulosPlanRepositorio.save(moduloPlan);
    }

    
}
