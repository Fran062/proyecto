package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resId")
    private Long id;

    @Column(name = "resCalificacion", nullable = false)
    private Integer calificacion; // 1 to 5

    @Column(name = "resComentario", columnDefinition = "TEXT")
    private String comentario;

    @ManyToOne
    @JoinColumn(name = "serId", nullable = false)
    @JsonIgnoreProperties("resenas")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "usrId", nullable = false)
    // Avoid circular recursion or too much data
    @JsonIgnoreProperties({ "password", "roles" })
    private Usuario usuario;

    public Resena() {
    }

    public Resena(Integer calificacion, String comentario, Servicio servicio, Usuario usuario) {
        this.calificacion = calificacion;
        this.comentario = comentario;
        this.servicio = servicio;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
