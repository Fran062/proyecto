package edu.serviClick.proyecto.dto;

import java.util.Date;

public class SuscripcionDTO {
    private Long id;
    private Long usuarioId;
    private String nombrePlan;
    private Double precioTotalMensual;
    private Date fechaInicio;
    private boolean activa;

    public SuscripcionDTO() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public Double getPrecioTotalMensual() {
        return precioTotalMensual;
    }

    public void setPrecioTotalMensual(Double precioTotalMensual) {
        this.precioTotalMensual = precioTotalMensual;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
