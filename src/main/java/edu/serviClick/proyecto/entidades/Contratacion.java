package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "contrataciones")
public class Contratacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "conId")
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "conFechaSolicitud")
    private Date fechaSolicitud;
    
    @Column(name = "conEstado")
    private String estado; 
    
    @Column(name = "conMensajeCliente")
    private String mensajeCliente;

    // Relación con Cliente
    @ManyToOne
    @JoinColumn(name = "usrId", nullable = false) 
    private Usuario cliente;

    // Relación con Servicio
    @ManyToOne
    @JoinColumn(name = "serId", nullable = false) 
    private Servicio servicio;

    public Contratacion() {
        this.fechaSolicitud = new Date();
        this.estado = "PENDIENTE";
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(Date fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Usuario getCliente() { return cliente; }
    public void setCliente(Usuario cliente) { this.cliente = cliente; }
    public Servicio getServicio() { return servicio; }
    public void setServicio(Servicio servicio) { this.servicio = servicio; }
    public String getMensajeCliente() { return mensajeCliente; }
    public void setMensajeCliente(String mensajeCliente) { this.mensajeCliente = mensajeCliente; }
}