package edu.backend_frontend_serviclick.dto;

public class UsuarioDTO {

    private Long id;
    private String nombreCompleto;
    private String correo;
    
    // IMPORTANTE: Se debe llamar 'password' porque así viene en el JSON de tu API
    // Aquí se guardará el hash encriptado ($2a$10$...)
    private String password; 
    
    private String rol;
    private String telefono;
    private String ubicacion;

    // Constructor vacío (Obligatorio para que Spring pueda crear el objeto)
    public UsuarioDTO() {
    }

    // --- Getters y Setters ---

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

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}