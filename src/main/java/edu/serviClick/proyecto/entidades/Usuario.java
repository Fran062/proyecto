package edu.serviClick.proyecto.entidades;

import jakarta.persistence.*;
import java.util.Date;
import edu.serviClick.proyecto.enums.Rol;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usr_id") 
    private Long id;

    @Column(name = "usr_nombre_completo", nullable = false, length = 50)
    private String nombreCompleto;

    @Column(name = "usr_correo", nullable = false, unique = true)
    private String correo;

    @Column(name = "usr_movil", length = 15)
    private String movil;

    @Column(name = "usr_password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "usr_rol", nullable = false)
    private Rol rol;
    
    @Column(name = "usr_activo")
    private boolean activo = false;


    @Column(name = "usr_token_recuperacion")
    private String token;

    @Temporal(TemporalType.DATE)
    @Column(name = "usr_fecha_alta")
    private Date fechaAlta;


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