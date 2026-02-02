package edu.backend_frontend_serviclick.ApiCliente;

import edu.backend_frontend_serviclick.dto.UsuarioDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiCliente {

    private final WebClient webClient;

    public ApiCliente(@Value("${api.base-url}") String apiUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(apiUrl).build();
    }

    public UsuarioDTO buscarUsuarioPorCorreo(String correo) {
        try {
            return webClient.post()
                    .uri("/usuarios/buscar-por-correo")
                    .bodyValue(correo) // Enviamos solo el correo
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error buscando usuario: " + e.getMessage());
            return null;
        }
    }

    public UsuarioDTO buscarUsuarioPorId(Long id) {
        try {
            return webClient.get()
                    .uri("/usuarios/" + id) // Asumiendo que la API tiene GET /usuarios/{id}
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error buscando usuario por ID: " + e.getMessage());
            return null;
        }
    }
}