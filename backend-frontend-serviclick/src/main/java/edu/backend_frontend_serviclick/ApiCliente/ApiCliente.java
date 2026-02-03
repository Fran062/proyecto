package edu.backend_frontend_serviclick.ApiCliente;

import edu.backend_frontend_serviclick.dto.UsuarioDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class ApiCliente {

    private final WebClient webClient;

    public ApiCliente(@Value("${api.base-url}") String apiUrl, WebClient.Builder builder) {
        if (apiUrl == null || apiUrl.isBlank()) {
            throw new IllegalStateException("FATAL: 'api.base-url' is not configured or is empty. Please check application.properties or environment variables.");
        }
        String cleanUrl = apiUrl.trim();
        System.out.println(">>> ApiCliente initialized. Target API URL: [" + cleanUrl + "]");
        this.webClient = builder.baseUrl(cleanUrl).build();
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

    public java.util.List<edu.backend_frontend_serviclick.dto.ServicioDTO> listarServicios() {
        try {
            return webClient.get()
                    .uri("/servicios") // GET /api/servicios
                    .retrieve()
                    .bodyToFlux(edu.backend_frontend_serviclick.dto.ServicioDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.out.println("Error listando servicios: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }
    public edu.backend_frontend_serviclick.dto.ServicioDTO buscarServicioPorId(Long id) {
        try {
           java.util.List<edu.backend_frontend_serviclick.dto.ServicioDTO> todos = listarServicios();
           return todos.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    public edu.backend_frontend_serviclick.dto.ServicioDTO crearServicio(edu.backend_frontend_serviclick.dto.ServicioDTO servicio) {
        try {
            return webClient.post()
                    .uri("/servicios")
                    .bodyValue(servicio)
                    .retrieve()
                    .bodyToMono(edu.backend_frontend_serviclick.dto.ServicioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error creando servicio: " + e.getMessage());
            return null;
        }
    }
}