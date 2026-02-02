package edu.backend_frontend_serviclick.servicios;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.dto.UsuarioDTO;
import edu.backend_frontend_serviclick.ApiCliente.ApiCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private ApiCliente apiCliente;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean realizarLogin(LoginDTO datosLogin) {
        // 1. Pedir a la API el usuario por correo (solo datos, sin validar pass)
        UsuarioDTO usuarioDeBaseDatos = apiCliente.buscarUsuarioPorCorreo(datosLogin.getCorreo());

        if (usuarioDeBaseDatos == null) {
            return false; // El usuario no existe
        }

        // 2. LA LÓGICA: Comparar la contraseña plana del login con el Hash de la BBDD
        // datosLogin.getContrasena() -> "1234"
        // usuarioDeBaseDatos.getPassword() -> "$2a$10$XyZ..."
        return passwordEncoder.matches(datosLogin.getContrasena(), usuarioDeBaseDatos.getPassword());
    }

    public UsuarioDTO buscarPorId(Long id) {
        return apiCliente.buscarUsuarioPorId(id);
    }
}