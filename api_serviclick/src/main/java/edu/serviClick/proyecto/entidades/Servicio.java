package edu.serviClick.proyecto.entidades;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "serId")
    private Long id;

    @Column(name = "serTitulo", nullable = false)
    private String titulo;

    @Column(name = "serDescripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "serPrecioHora")
    private Double precioHora;

    @Column(name = "serCategoria")
    private String categoria;

    @ManyToOne
    @JoinColumn(name = "usrId", nullable = false)
    @JsonIgnoreProperties({ "password", "serviciosOfrecidos", "historialContrataciones", "suscripcion",
            "codigoRecuperacion", "fechaExpiracionCodigo", "tokenVerificacion", "habilitado" })
    private Usuario profesional;

    @Column(name = "serImagen", columnDefinition = "TEXT")
    private String imagen;

    @Transient
    private Double promedio;

    public Servicio() {
    }

    // Getters y Setters
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

    public Usuario getProfesional() {
        return profesional;
    }

    public void setProfesional(Usuario profesional) {
        this.profesional = profesional;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }
}