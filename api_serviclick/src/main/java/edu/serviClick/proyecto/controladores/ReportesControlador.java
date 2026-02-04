package edu.serviClick.proyecto.controladores;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PathVariable;
import edu.serviClick.proyecto.repositorios.UsuariosRepositorio;
import edu.serviClick.proyecto.entidades.Usuario;
import edu.serviClick.proyecto.entidades.Contratacion;

@RestController
@RequestMapping("/api/reportes")
public class ReportesControlador {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @GetMapping("/factura/{usuarioId}")
    public void generarFacturaUsuario(@PathVariable Long usuarioId, HttpServletResponse response)
            throws IOException, DocumentException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=factura_" + usuarioId + "_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        // Buscar usuario y sus datos
        Usuario usuario = usuarioRepositorio.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Fuentes
        com.lowagie.text.Font fontTitle = com.lowagie.text.FontFactory
                .getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        com.lowagie.text.Font fontHeader = com.lowagie.text.FontFactory
                .getFont(com.lowagie.text.FontFactory.HELVETICA_BOLD);
        fontHeader.setSize(14);
        com.lowagie.text.Font fontNormal = com.lowagie.text.FontFactory.getFont(com.lowagie.text.FontFactory.HELVETICA);
        fontNormal.setSize(12);

        // Título
        Paragraph title = new Paragraph("Factura / Resumen de Cuenta", fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph(" "));

        // Datos del Cliente
        document.add(new Paragraph("Cliente: " + usuario.getNombreCompleto(), fontNormal));
        document.add(new Paragraph("Correo: " + usuario.getCorreo(), fontNormal));
        document.add(new Paragraph("Fecha de emisión: " + new Date().toString(), fontNormal));
        document.add(new Paragraph("------------------------------------------------"));
        document.add(new Paragraph(" "));

        // 1. Servicios Contratados
        document.add(new Paragraph("Historial de Servicios Contratados", fontHeader));
        document.add(new Paragraph(" ", fontNormal));

        List<Contratacion> contrataciones = usuario.getHistorialContrataciones();

        if (contrataciones == null || contrataciones.isEmpty()) {
            document.add(new Paragraph("No ha contratado servicios aún.", fontNormal));
        } else {
            for (Contratacion contrato : contrataciones) {
                if (contrato.getServicio() != null) {
                    document.add(new Paragraph("Servicio: " + contrato.getServicio().getTitulo(), fontNormal));
                    document.add(new Paragraph("Fecha Solicitud: "
                            + (contrato.getFechaSolicitud() != null ? contrato.getFechaSolicitud().toString() : "N/A"),
                            fontNormal));
                    document.add(
                            new Paragraph("Precio hora: " + contrato.getServicio().getPrecioHora() + " €", fontNormal));

                    // Lógica para estado en PDF: Si es PENDIENTE y pasaron 24h, mostrar COMPLETADO
                    String estadoMostrar = contrato.getEstado();
                    if ("PENDIENTE".equalsIgnoreCase(estadoMostrar)) {
                        if (contrato.getFechaSolicitud() != null &&
                                contrato.getFechaSolicitud().plusHours(24).isBefore(java.time.LocalDateTime.now())) {
                            estadoMostrar = "COMPLETADO";
                        }
                    }
                    document.add(new Paragraph("Estado: " + estadoMostrar, fontNormal));

                    document.add(new Paragraph("- - - - - - - - - - - - - - - - - - - - - - - -"));
                }
            }
        }

        document.add(new Paragraph(" "));
        document.add(new Paragraph("------------------------------------------------"));
        document.add(new Paragraph(" "));

        // 2. Plan / Suscripción
        if (usuario.getSuscripcion() != null && usuario.getSuscripcion().isActiva()) {
            document.add(new Paragraph("Suscripción Activa", fontHeader));
            document.add(new Paragraph(" ", fontNormal));
            document.add(new Paragraph("Plan: " + usuario.getSuscripcion().getNombrePlan(), fontNormal));
            document.add(new Paragraph("Precio Mensual: " + usuario.getSuscripcion().getPrecioTotalMensual() + " €",
                    fontNormal));
            document.add(new Paragraph("Fecha Inicio: " + (usuario.getSuscripcion().getFechaInicio() != null
                    ? usuario.getSuscripcion().getFechaInicio().toString()
                    : "N/A"), fontNormal));
        } else {
            // Si no tiene plan, no mostrar nada (según requisito) o indicar 'Sin plan
            // activo'
            // Requisito: "si el usuario no tiene seleccionado ningun plan no se pone nada"
        }

        document.close();
    }
}
