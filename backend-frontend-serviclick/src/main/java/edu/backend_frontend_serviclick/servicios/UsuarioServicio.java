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

    /**
     * Valida las credenciales del usuario comparando la contraseña ingresada con el hash almacenado.
     * @param datosLogin DTO con correo y contraseña plana.
     * @return El usuario si la autenticación es correcta y la cuenta activada; null en caso contrario.
     */
    public UsuarioDTO realizarLogin(LoginDTO datosLogin) {
        // 1. Pedir a la API el usuario por correo (solo datos, sin validar pass)
        UsuarioDTO usuarioDeBaseDatos = apiCliente.buscarUsuarioPorCorreo(datosLogin.getCorreo());

        if (usuarioDeBaseDatos == null) {
            return null; // El usuario no existe
        }

        // 2. Verificar si la cuenta está habilitada
        if (!Boolean.TRUE.equals(usuarioDeBaseDatos.getHabilitado())) {
            System.out.println("Usuario no habilitado. Debe confirmar su correo.");
            return null; // Cuenta no activada
        }

        // 3. LA LÓGICA: Comparar la contraseña plana del login con el Hash de la BBDD
        if (passwordEncoder.matches(datosLogin.getContrasena(), usuarioDeBaseDatos.getPassword())) {
            return usuarioDeBaseDatos;
        }
        return null;
    }

    /**
     * Busca un usuario por su identificador único.
     * @param id ID del usuario.
     * @return DTO del usuario encontrado.
     */
    public UsuarioDTO buscarPorId(Long id) {
        return apiCliente.buscarUsuarioPorId(id);
    }

    /**
     * Envía los datos de un nuevo usuario a la API para su registro.
     * @param usuario DTO con los datos del usuario.
     * @return El usuario creado retornado por la API.
     */
    public UsuarioDTO registrarUsuario(UsuarioDTO usuario) {
        // La API se encarga de hashear la contraseña
        return apiCliente.crearUsuario(usuario);
    }
}