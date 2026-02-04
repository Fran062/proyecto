package edu.serviClick.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

import java.time.LocalDateTime;
import edu.serviClick.proyecto.enums.Rol;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usrId")
    private Long id;

    @Column(name = "usrNombreCompleto", nullable = false, length = 50)
    @jakarta.validation.constraints.Size(max = 50, message = "El nombre no puede exceder 50 caracteres")
    private String nombreCompleto;

    @Column(name = "usrCorreo", unique = true, nullable = false)
    @jakarta.validation.constraints.Email(message = "Debe ser un correo válido")
    private String correo;

    @Column(name = "usrPassword", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usrRol")
    private Rol rol;

    @Column(name = "usrTelefono")
    @jakarta.validation.constraints.Pattern(regexp = "^(\\+34|0034|34)?[6789]\\d{8}$", message = "Debe ser un teléfono español válido")
    private String telefono;

    @Column(name = "usrUbicacion")
    private String ubicacion;

    @Column(name = "usrHabilitado")
    private Boolean habilitado = false; // Default false for email confirmation

    @Column(name = "usrFechaRegistro")
    private LocalDateTime fechaRegistro;

    @PrePersist
    public void prePersist() {
        if (fechaRegistro == null) {
            fechaRegistro = LocalDateTime.now();
        }
    }

    // Relaciones

    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Servicio> serviciosOfrecidos;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Contratacion> historialContrataciones;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    @Column(name = "usrCodigoRecuperacion")
    private String codigoRecuperacion;

    @Column(name = "usrTokenVerificacion")
    private String tokenVerificacion;

    @Column(name = "usrFechaExpiracionCodigo")
    private java.time.LocalDateTime fechaExpiracionCodigo;

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

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

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

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public String getTokenVerificacion() {
        return tokenVerificacion;
    }

    public void setTokenVerificacion(String tokenVerificacion) {
        this.tokenVerificacion = tokenVerificacion;
    }

    public List<Contratacion> getHistorialContrataciones() {
        return historialContrataciones;
    }

    public void setHistorialContrataciones(List<Contratacion> historialContrataciones) {
        this.historialContrataciones = historialContrataciones;
    }

    public List<Servicio> getServiciosOfrecidos() {
        return serviciosOfrecidos;
    }

    public void setServiciosOfrecidos(List<Servicio> serviciosOfrecidos) {
        this.serviciosOfrecidos = serviciosOfrecidos;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
