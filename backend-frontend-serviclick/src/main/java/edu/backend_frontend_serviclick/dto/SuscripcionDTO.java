package edu.backend_frontend_serviclick.dto;

public class SuscripcionDTO {
    private Long id;
    private String nombrePlan;
    private Double precioTotalMensual;
    private boolean activa;

    public SuscripcionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }
}
