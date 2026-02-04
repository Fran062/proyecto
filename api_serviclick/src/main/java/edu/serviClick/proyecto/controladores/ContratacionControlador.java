package edu.serviClick.proyecto.controladores;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.serviClick.proyecto.entidades.Contratacion;
import edu.serviClick.proyecto.entidades.Servicio;
import edu.serviClick.proyecto.entidades.Usuario;
import edu.serviClick.proyecto.servicios.ContratacionServicio;
import edu.serviClick.proyecto.servicios.ServiciosService;
import edu.serviClick.proyecto.servicios.UsuarioServicio;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/contrataciones")
public class ContratacionControlador {

    private static final Logger logger = LoggerFactory.getLogger(ContratacionControlador.class);

    @Autowired
    private ContratacionServicio contratacionServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ServiciosService serviciosService;

    @GetMapping
    public List<Contratacion> obtenerTodas() {
        return contratacionServicio.buscarTodasLasContrataciones();
    }

    @PostMapping("/crear")
    public ResponseEntity<Contratacion> crearContratacion(@RequestBody Map<String, Object> payload) {
        try {
            Long usuarioId = Long.parseLong(payload.get("usuarioId").toString());
            Long servicioId = Long.parseLong(payload.get("servicioId").toString());

            logger.info("Solicitud de nueva contratación. ClienteID: {}, ServicioID: {}", usuarioId, servicioId);

            // Buscar usuario y servicio
            Usuario cliente = usuarioServicio.buscarPorId(usuarioId);
            Servicio servicio = serviciosService.buscarPorId(servicioId);
            // devuelve Optional

            if (cliente == null || servicio == null) {
                logger.error(
                        "Error al crear contratación: Cliente o servicio no encontrados. ClienteID: {}, ServicioID: {}",
                        usuarioId, servicioId);
                return ResponseEntity.badRequest().build();
            }

            Contratacion contratacion = new Contratacion();
            contratacion.setCliente(cliente);
            contratacion.setServicio(servicio);
            contratacion.setFechaSolicitud(LocalDateTime.now());
            contratacion.setEstado("Pendiente");
            contratacion.setMensajeCliente("Solicitud de contratación de servicio");

            Contratacion guardada = contratacionServicio.guardarContratacion(contratacion);
            logger.info("Contratación creada exitosamente con ID: {}", guardada.getId());
            return ResponseEntity.ok(guardada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

}