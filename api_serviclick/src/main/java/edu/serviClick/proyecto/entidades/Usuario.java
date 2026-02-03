package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usrId")
    private Long id;

    @Column(name = "usrNombreCompleto", nullable = false)
    private String nombreCompleto;

    @Column(name = "usrCorreo", unique = true, nullable = false)
    private String correo;

    @Column(name = "usrPassword", nullable = false)
    private String password;

    @Column(name = "usrRol")
    private String rol;

    @Column(name = "usrTelefono")
    private String telefono;

    @Column(name = "usrUbicacion")
    private String ubicacion;

    // Relaciones

    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
    private List<Servicio> serviciosOfrecidos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Contratacion> historialContrataciones;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    public Usuario() {
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Column(name = "usrCodigoRecuperacion")
    private String codigoRecuperacion;

    @Column(name = "usrFechaExpiracionCodigo")
    private java.time.LocalDateTime fechaExpiracionCodigo;

    public String getCodigoRecuperacion() {
        return codigoRecuperacion;
    }

    public void setCodigoRecuperacion(String codigoRecuperacion) {
        this.codigoRecuperacion = codigoRecuperacion;
    }

    public java.time.LocalDateTime getFechaExpiracionCodigo() {
        return fechaExpiracionCodigo;
    }

    public void setFechaExpiracionCodigo(java.time.LocalDateTime fechaExpiracionCodigo) {
        this.fechaExpiracionCodigo = fechaExpiracionCodigo;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Suscripcion getSuscripcion() {
        return suscripcion;
    }

    public void setSuscripcion(Suscripcion suscripcion) {
        this.suscripcion = suscripcion;
    }
}