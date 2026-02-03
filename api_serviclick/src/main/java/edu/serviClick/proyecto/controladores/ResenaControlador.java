package edu.serviClick.proyecto.controladores;

import edu.serviClick.proyecto.entidades.Resena;
import edu.serviClick.proyecto.servicios.ResenaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resenas")
public class ResenaControlador {

    @Autowired
    private ResenaServicio resenaServicio;

    @GetMapping("/servicio/{servicioId}")
    public List<Resena> obtenerResenas(@PathVariable Long servicioId) {
        return resenaServicio.obtenerResenasPorServicio(servicioId);
    }

    @GetMapping("/servicio/{servicioId}/promedio")
    public Double obtenerPromedio(@PathVariable Long servicioId) {
        return resenaServicio.calcularPromedioCalificacion(servicioId);
    }

    @PostMapping
    public ResponseEntity<Resena> crearResena(@RequestBody Resena resena) {
        // Here we assume the passed Resena object has properly populated Servicio and
        // Usuario objects (at least IDs)
        // Spring Data JPA + Hibernate often handles { "id": X } references if
        // configured,
        // but to be safe, valid validations should exist.
        // For MVP:
        try {
            Resena nueva = resenaServicio.guardarResena(resena);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarResena(@PathVariable Long id) {
        try {
            resenaServicio.eliminarResena(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
