package edu.backend_frontend_serviclick.dto;

public class ServicioDTO {

    private Long id;
    private String titulo;
    private String descripcion;
    private Double precioHora;
    private String categoria;
    private String imagen;

    // El profesional que ofrece el servicio (UsuarioDTO ya existe)
    private UsuarioDTO profesional;

    public ServicioDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(Double precioHora) {
        this.precioHora = precioHora;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public UsuarioDTO getProfesional() {
        return profesional;
    }

    public void setProfesional(UsuarioDTO profesional) {
        this.profesional = profesional;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    private Double promedio;

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}
