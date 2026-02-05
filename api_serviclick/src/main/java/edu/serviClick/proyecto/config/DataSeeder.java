package edu.serviClick.proyecto.configuracion;

import java.time.LocalDateTime;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import edu.serviClick.proyecto.entidades.*;
import edu.serviClick.proyecto.enums.Rol;
import edu.serviClick.proyecto.repositorios.*;

@Configuration
public class DataSeeder {

    @Autowired
    private UsuariosRepositorio usuarioRepositorio;

    @Autowired
    private ServiciosRepositorio serviciosRepositorio;

    @Autowired
    private SuscripcionesRepositorio suscripcionesRepositorio;

    @Autowired
    private ResenaRepositorio resenaRepositorio;

    @Autowired
    private ContratacionesRepositorio contratacionesRepositorio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {
            System.out.println("--- DATA SEEDER: Iniciando carga de datos iniciales ---");

            // 1. Crear Usuario ADMIN
            if (usuarioRepositorio.findByCorreo("serviclicksoporte@gmail.com").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombreCompleto("Soporte ServiClick");
                admin.setCorreo("serviclicksoporte@gmail.com");
                admin.setPassword(passwordEncoder.encode("admin123")); // Contraseña segura :)
                admin.setRol(Rol.ADMIN);
                admin.setHabilitado(true);
                admin.setTelefono("600123456");
                admin.setFechaRegistro(LocalDateTime.now());
                usuarioRepositorio.save(admin);
                System.out.println("Usuario Admin creado: serviclicksoporte@gmail.com");
            }

            // 2. Crear Profesional de Ejemplo (Para los servicios)
            Usuario profesional = usuarioRepositorio.findByCorreo("profesional@ejemplo.com").orElse(null);
            if (profesional == null) {
                profesional = new Usuario();
                profesional.setNombreCompleto("Juan Profesional");
                profesional.setCorreo("profesional@ejemplo.com");
                profesional.setPassword(passwordEncoder.encode("123456"));
                profesional.setRol(Rol.PROFESIONAL);
                profesional.setHabilitado(true);
                profesional.setTelefono("666777888");
                profesional.setFechaRegistro(LocalDateTime.now());
                profesional = usuarioRepositorio.save(profesional);
                System.out.println("Usuario Profesional creado: profesional@ejemplo.com");

                // Suscripción para el profesional
                Suscripcion sus = new Suscripcion();
                sus.setUsuario(profesional);
                sus.setNombrePlan("PROFESIONAL");
                sus.setActiva(true);
                sus.setFechaInicio(new Date());
                sus.setPrecioTotalMensual(29.99);
                suscripcionesRepositorio.save(sus);
            }

            // 3. Crear Cliente de Ejemplo
            Usuario cliente = usuarioRepositorio.findByCorreo("cliente@ejemplo.com").orElse(null);
            if (cliente == null) {
                cliente = new Usuario();
                cliente.setNombreCompleto("Ana Cliente");
                cliente.setCorreo("cliente@ejemplo.com");
                cliente.setPassword(passwordEncoder.encode("123456"));
                cliente.setRol(Rol.CLIENTE);
                cliente.setHabilitado(true);
                cliente.setTelefono("611222333");
                cliente.setFechaRegistro(LocalDateTime.now());
                cliente = usuarioRepositorio.save(cliente);
                System.out.println("Usuario Cliente creado: cliente@ejemplo.com");
            }

            // 4. Crear Servicios (Si no existen)
            if (serviciosRepositorio.count() == 0 && profesional != null) {
                // Servicio 1: Fontanería
                Servicio s1 = new Servicio();
                s1.setTitulo("Reparaciones de Fontanería Express");
                s1.setDescripcion("Servicio rápido de fontanería 24h. Reparación de fugas, grifos y tuberías.");
                s1.setPrecioHora(45.0);
                s1.setCategoria("Fontanería");
                s1.setProfesional(profesional);
                s1.setImagen(
                        "https://images.unsplash.com/photo-1585704032915-c3400ca199e7?q=80&w=2070&auto=format&fit=crop");
                serviciosRepositorio.save(s1);

                // Servicio 2: Electricidad
                Servicio s2 = new Servicio();
                s2.setTitulo("Instalaciones Eléctricas Domésticas");
                s2.setDescripcion(
                        "Todo tipo de instalaciones eléctricas, enchufes, iluminación LED y cuadros eléctricos.");
                s2.setPrecioHora(50.0);
                s2.setCategoria("Electricidad");
                s2.setProfesional(profesional);
                s2.setImagen(
                        "https://images.unsplash.com/photo-1621905251189-08b45d6a269e?q=80&w=2069&auto=format&fit=crop");
                serviciosRepositorio.save(s2);

                // Servicio 3: Limpieza
                Servicio s3 = new Servicio();
                s3.setTitulo("Limpieza a fondo de oficinas");
                s3.setDescripcion("Servicio de limpieza profesional para oficinas y locales comerciales.");
                s3.setPrecioHora(25.0);
                s3.setCategoria("Limpieza");
                s3.setProfesional(profesional);
                s3.setImagen(
                        "https://images.unsplash.com/photo-1581578731117-104f8a746950?q=80&w=1999&auto=format&fit=crop");
                serviciosRepositorio.save(s3);

                System.out.println("Servicios de ejemplo creados.");

                // 5. Crear Reseña y Contratación (Para que las tablas tengan datos)
                if (cliente != null) {
                    // Contratación
                    Contratacion contratacion = new Contratacion();
                    contratacion.setCliente(cliente);
                    contratacion.setServicio(s1);
                    contratacion.setEstado("COMPLETA");
                    contratacion.setFechaSolicitud(LocalDateTime.now().minusDays(5));
                    contratacion.setMensajeCliente("Necesito arreglar un grifo urgentemente.");
                    contratacionesRepositorio.save(contratacion);

                    // Reseña
                    Resena r = new Resena();
                    r.setCalificacion(5);
                    r.setComentario("Excelente servicio, muy rápido y profesional.");
                    r.setServicio(s1);
                    r.setUsuario(cliente);
                    resenaRepositorio.save(r);

                    System.out.println("Contrataciones y Reseñas de ejemplo creadas.");
                }
            }

            System.out.println("--- DATA SEEDER: Carga completada ---");
        };
    }
}
