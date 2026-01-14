package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "modulosplan") // He quitado el guion bajo del nombre de la tabla por si acaso
public class ModuloPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "modId")
    private Long id;

    @Column(name = "modNombre")
    private String nombre;

    @Column(name = "modDescripcion")
    private String descripcion;

    @Column(name = "modPrecioMensual")
    private Double precioMensual;

    @Column(name = "modIcono")
    private String icono;

    public ModuloPlan() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getPrecioMensual() { return precioMensual; }
    public void setPrecioMensual(Double precioMensual) { this.precioMensual = precioMensual; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
}