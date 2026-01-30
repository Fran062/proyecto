package edu.backend_frontend_serviclick.ApiCliente;

import edu.backend_frontend_serviclick.dto.LoginDTO;
import edu.backend_frontend_serviclick.dto.LoginRespuestaDTO;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Service
public class ApiCliente {

    private final WebClient webClient;

    public ApiCliente(@Value("${api.base-url}") String apiUrl, WebClient.Builder builder) {
        this.webClient = builder.baseUrl(apiUrl).build();
    }

    public LoginRespuestaDTO login(LoginDTO loginDto) {
        try {
            return webClient.post()
                    .uri("/usuarios/login")
                    .bodyValue(loginDto)
                    .retrieve()
                    .bodyToMono(LoginRespuestaDTO.class)
                    .block(); 
        } catch (WebClientResponseException e) {
            System.out.println("Error de Login: " + e.getStatusCode());
            return null;
        } catch (Exception e) {
            System.out.println("Error de conexión con la API");
            return null;
        }
    }
}