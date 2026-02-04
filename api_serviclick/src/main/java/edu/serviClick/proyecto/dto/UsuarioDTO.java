package edu.serviClick.proyecto.dto;

import java.time.LocalDateTime;
import edu.serviClick.proyecto.enums.Rol;

public class UsuarioDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private String telefono;
    private Boolean habilitado;
    private Rol rol;
    private LocalDateTime fechaRegistro;

    // Constructores
    public UsuarioDTO() {
    }

    public UsuarioDTO(Long id, String nombreCompleto, String correo, String telefono, Boolean habilitado, Rol rol,
            LocalDateTime fechaRegistro) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.telefono = telefono;
        this.habilitado = habilitado;
        this.rol = rol;
        this.fechaRegistro = fechaRegistro;
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

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
