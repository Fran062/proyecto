package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "suscripciones")
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "susId")
    private Long id;

    @OneToOne
    @JoinColumn(name = "usrId", nullable = false)
    @com.fasterxml.jackson.annotation.JsonIgnore
    private Usuario usuario;

    @Temporal(TemporalType.DATE)
    @Column(name = "susFechaInicio")
    private Date fechaInicio;

    @Column(name = "suspreciototalmensual")
    private Double precioTotalMensual;

    @Column(name = "susNombrePlan")
    private String nombrePlan;

    @Column(name = "susActiva")
    private boolean activa;

    public Suscripcion() {
        this.fechaInicio = new Date();
        this.activa = true;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Double getPrecioTotalMensual() {
        return precioTotalMensual;
    }

    public void setPrecioTotalMensual(Double precioTotalMensual) {
        this.precioTotalMensual = precioTotalMensual;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}