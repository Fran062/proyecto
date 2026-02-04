package edu.serviClick.proyecto.dto;

import java.time.LocalDateTime;

public class ContratacionDTO {
    private Long id;
    private Long clienteId;
    private String nombreCliente;
    private Long servicioId;
    private String tituloServicio;
    private String estado;
    private LocalDateTime fechaSolicitud;
    private String mensajeCliente;

    public ContratacionDTO() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public Long getServicioId() {
        return servicioId;
    }

    public void setServicioId(Long servicioId) {
        this.servicioId = servicioId;
    }

    public String getTituloServicio() {
        return tituloServicio;
    }

    public void setTituloServicio(String tituloServicio) {
        this.tituloServicio = tituloServicio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaSolicitud() {
        return fechaSolicitud;
    }

    public void setFechaSolicitud(LocalDateTime fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    public String getMensajeCliente() {
        return mensajeCliente;
    }

    public void setMensajeCliente(String mensajeCliente) {
        this.mensajeCliente = mensajeCliente;
    }
}
