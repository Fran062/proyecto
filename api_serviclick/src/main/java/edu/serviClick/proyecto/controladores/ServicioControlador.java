package edu.serviClick.proyecto.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/{id}")
    public ResponseEntity<Servicio> obtenerServicioPorId(@PathVariable Long id) {
        Servicio servicio = serviciosService.buscarPorId(id);
        if (servicio != null) {
            return ResponseEntity.ok(servicio);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarServicio(@PathVariable Long id) {
        try {
            serviciosService.eliminarServicio(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public ResponseEntity<Servicio> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicio) {
        Servicio existente = serviciosService.buscarPorId(id);
        if (existente == null) {
            return ResponseEntity.notFound().build();
        }
        // Aseguramos que el ID y el profesional se mantengan (o actualizar según lógica
        // negocio)
        // Por simplicidad, actualizamos campos básicos:
        if (servicio.getTitulo() != null)
            existente.setTitulo(servicio.getTitulo());
        if (servicio.getDescripcion() != null)
            existente.setDescripcion(servicio.getDescripcion());
        if (servicio.getPrecioHora() != null)
            existente.setPrecioHora(servicio.getPrecioHora());
        if (servicio.getCategoria() != null)
            existente.setCategoria(servicio.getCategoria());
        if (servicio.getImagen() != null)
            existente.setImagen(servicio.getImagen());

        Servicio actualizado = serviciosService.guardarServicio(existente);
        return ResponseEntity.ok(actualizado);
    }

}