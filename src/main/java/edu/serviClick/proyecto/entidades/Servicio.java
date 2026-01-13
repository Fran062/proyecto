package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="serId")
    private Long id;

    @Column(name="serTitulo", nullable = false)
    private String titulo;

    @Column(name="serDescripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name="serPrecioHora")
    private Double precioHora;

    @Column(name="serCategoria")
    private String categoria;

    // Relación: Un servicio pertenece a un Profesional
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario profesional;

    public Servicio() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Double getPrecioHora() { return precioHora; }
    public void setPrecioHora(Double precioHora) { this.precioHora = precioHora; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Usuario getProfesional() { return profesional; }
    public void setProfesional(Usuario profesional) { this.profesional = profesional; }
}