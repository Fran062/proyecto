package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

import edu.serviClick.proyecto.enums.Rol;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usrId") 
    private Long id;

    @Column(name = "usrNombreCompleto", nullable = false, length = 50)
    private String nombreCompleto;

    @Column(name = "usrCorreo", nullable = false, unique = true)
    private String correo;

    @Column(name = "usrMovil", length = 15)
    private String movil;

    @Column(name = "usrPassword", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usrRol", nullable = false)
    private Rol rol;
    
    @Column(name = "usrActivo")
    private boolean activo = false;


    @Column(name = "usrTokenRecuperacion")
    private String token;

    @Temporal(TemporalType.DATE)
    @Column(name = "usrFechaAlta")
    private Date fechaAlta;


    //Relaciones

    // Un profesional puede publicar muchos servicios
    @OneToMany(mappedBy = "profesional", cascade = CascadeType.ALL)
    private List<Servicio> serviciosOfrecidos;

    // Un cliente puede hacer muchas contrataciones
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Contratacion> historialContrataciones;

    // INNOVACIÓN: Un usuario puede tener UNA suscripción activa
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Suscripcion suscripcion;

    //Constructor
    public Usuario() {
        this.fechaAlta = new Date();
    }

    //Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getMovil() { return movil; }
    public void setMovil(String movil) { this.movil = movil; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Date getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(Date fechaAlta) { this.fechaAlta = fechaAlta; }

}