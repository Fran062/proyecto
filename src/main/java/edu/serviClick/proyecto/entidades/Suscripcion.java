package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "suscripciones")
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="susId")
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // AQUÍ ESTÁ LA MAGIA: Muchos a Muchos
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "suscripcion_modulos",
        joinColumns = @JoinColumn(name = "suscripcion_id"),
        inverseJoinColumns = @JoinColumn(name = "modulo_id")
    )
    private List<ModuloPlan> modulosContratados;

    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    
    private Double precioTotalMensual;
    private boolean activa;

    public Suscripcion() {
        this.fechaInicio = new Date();
        this.activa = true;
    }

    // Calcula el precio sumando los módulos
    public void calcularPrecio() {
        if (modulosContratados != null) {
            this.precioTotalMensual = modulosContratados.stream()
                .mapToDouble(ModuloPlan::getPrecioMensual)
                .sum();
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public List<ModuloPlan> getModulosContratados() { return modulosContratados; }
    public void setModulosContratados(List<ModuloPlan> modulosContratados) { this.modulosContratados = modulosContratados; }
    public Double getPrecioTotalMensual() { return precioTotalMensual; }
    public void setPrecioTotalMensual(Double precioTotalMensual) { this.precioTotalMensual = precioTotalMensual; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
}