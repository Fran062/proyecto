package edu.backend_frontend_serviclick.servicios;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.dto.LoginRespuestaDTO;
import edu.backend_frontend_serviclick.ApiCliente.ApiCliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicio {

    @Autowired
    private ApiCliente apiCliente;

    public LoginRespuestaDTO realizarLogin(LoginDTO datos) {
        return apiCliente.login(datos);
    }
}