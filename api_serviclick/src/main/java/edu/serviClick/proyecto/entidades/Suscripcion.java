package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "suscripciones")
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "susId")
    private Long id;

    // La columna en BBDD que guarda el ID del usuario (clave foránea)
    @OneToOne
    @JoinColumn(name = "usrId", nullable = false)
    private Usuario usuario;

    // TABLA INTERMEDIA
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "suscripcionmodulos", joinColumns = @JoinColumn(name = "susId"), inverseJoinColumns = @JoinColumn(name = "modId"))
    private List<ModuloPlan> modulosContratados;

    @Temporal(TemporalType.DATE)
    @Column(name = "susFechaInicio")
    private Date fechaInicio;

    @Column(name = "suspreciototalmensual")
    private Double precioTotalMensual;

    @Column(name = "susActiva")
    private boolean activa;

    public Suscripcion() {
        this.fechaInicio = new Date();
        this.activa = true;
    }

    @PrePersist
    public void antesDeGuardar() {
        this.calcularPrecio();
    }

    
    public void calcularPrecio() {
        if (modulosContratados != null && !modulosContratados.isEmpty()) {
            this.precioTotalMensual = modulosContratados.stream()
                    .filter(m -> m.getPrecioMensual() != null)
                    .mapToDouble(ModuloPlan::getPrecioMensual)
                    .sum();
        } else {
            this.precioTotalMensual = 0.0;
        }
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

    public List<ModuloPlan> getModulosContratados() {
        return modulosContratados;
    }

    public void setModulosContratados(List<ModuloPlan> modulosContratados) {
        this.modulosContratados = modulosContratados;
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

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
}