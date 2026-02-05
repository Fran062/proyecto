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
            throw new IllegalStateException(
                    "FATAL: 'api.base-url' is not configured or is empty. Please check application.properties or environment variables.");
        }
        String cleanUrl = apiUrl.trim();
        System.out.println(">>> ApiCliente initialized. Target API URL: [" + cleanUrl + "]");
        this.webClient = builder
                .baseUrl(cleanUrl)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(16 * 1024 * 1024))
                .build();
    }

    /**
     * Busca un usuario por correo electrónico llamando a la API.
     * @param correo Correo del usuario.
     * @return DTO del usuario encontrado o null.
     */
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

    /**
     * Busca un usuario por ID llamando a la API.
     * @param id ID del usuario.
     * @return DTO del usuario o null.
     */
    public UsuarioDTO buscarUsuarioPorId(Long id) {
        try {
            return webClient.get()
                    .uri("/usuarios/" + id)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error buscando usuario por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Recupera el listado completo de servicios desde la API.
     * @return Lista de ServiciosDTO.
     */
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

    /**
     * Busca un servicio específico por su ID.
     * @param id ID del servicio.
     * @return DTO del servicio o null.
     */
    public edu.backend_frontend_serviclick.dto.ServicioDTO buscarServicioPorId(Long id) {
        try {
            java.util.List<edu.backend_frontend_serviclick.dto.ServicioDTO> todos = listarServicios();
            return todos.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Envía una solicitud para crear un nuevo servicio.
     * @param servicio Datos del nuevo servicio.
     * @return DTO del servicio creado.
     */
    public edu.backend_frontend_serviclick.dto.ServicioDTO crearServicio(
            edu.backend_frontend_serviclick.dto.ServicioDTO servicio) {
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

    /**
     * Registra un nuevo usuario en la API.
     * @param usuario Datos del usuario.
     * @return DTO del usuario creado.
     */
    public UsuarioDTO crearUsuario(UsuarioDTO usuario) {
        try {
            return webClient.post()
                    .uri("/usuarios") // POST /api/usuarios
                    .bodyValue(usuario)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error creando usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtiene las reseñas de un servicio específico.
     * @param servicioId ID del servicio.
     * @return Lista de reseñas.
     */
    public java.util.List<edu.backend_frontend_serviclick.dto.ResenaDTO> obtenerResenasPorServicio(Long servicioId) {
        try {
            return webClient.get()
                    .uri("/resenas/servicio/" + servicioId)
                    .retrieve()
                    .bodyToFlux(edu.backend_frontend_serviclick.dto.ResenaDTO.class)
                    .collectList()
                    .block();
        } catch (Exception e) {
            System.out.println("Error obteniendo reseñas: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    /**
     * Obtiene el promedio de calificación de un servicio.
     * @param servicioId ID del servicio.
     * @return Valor promedio.
     */
    public Double obtenerPromedioServicio(Long servicioId) {
        try {
            return webClient.get()
                    .uri("/resenas/servicio/" + servicioId + "/promedio")
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();
        } catch (Exception e) {
            return 0.0;
        }
    }

    /**
     * Crea una nueva reseña para un servicio.
     * @param resena Datos de la reseña.
     * @return DTO de la reseña creada.
     */
    public edu.backend_frontend_serviclick.dto.ResenaDTO crearResena(
            edu.backend_frontend_serviclick.dto.ResenaDTO resena) {
        try {
            return webClient.post()
                    .uri("/resenas")
                    .bodyValue(resena)
                    .retrieve()
                    .bodyToMono(edu.backend_frontend_serviclick.dto.ResenaDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error creando reseña: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina una reseña por su ID.
     * @param resenaId ID de la reseña.
     */
    public void eliminarResena(Long resenaId) {
        try {
            webClient.delete()
                    .uri("/resenas/" + resenaId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            System.out.println("Error eliminando reseña: " + e.getMessage());
        }
    }

    /**
     * Actualiza la información de un usuario.
     * @param id ID del usuario.
     * @param usuario Datos actualizados.
     * @return DTO del usuario actualizado.
     */
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuario) {
        try {
            return webClient.put()
                    .uri("/usuarios/" + id)
                    .bodyValue(usuario)
                    .retrieve()
                    .bodyToMono(UsuarioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error actualizando usuario: " + e.getMessage());
            return null;
        }
    }

    /**
     * Solicita la recuperación de contraseña.
     * @param correo Correo electrónico.
     */
    public void solicitarRecuperacionPass(String correo) {
        try {
            webClient.post()
                    .uri("/usuarios/recuperar-password/solicitar")
                    .bodyValue(correo)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            System.out.println("Error solicitando recuperación: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Verifica el código de recuperación.
     * @param correo Correo electrónico.
     * @param codigo Código.
     * @return true si es válido.
     */
    public boolean verificarCodigoRecuperacion(String correo, String codigo) {
        try {
            java.util.Map<String, String> payload = new java.util.HashMap<>();
            payload.put("correo", correo);
            payload.put("codigo", codigo);
            return Boolean.TRUE.equals(webClient.post()
                    .uri("/usuarios/recuperar-password/verificar")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block());
        } catch (Exception e) {
            System.out.println("Error verificando código: " + e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la contraseña con el código de verificación.
     * @param correo Correo electrónico.
     * @param codigo Código.
     * @param nuevaPassword Nueva contraseña.
     */
    public void cambiarPasswordConCodigo(String correo, String codigo, String nuevaPassword) {
        try {
            java.util.Map<String, String> payload = new java.util.HashMap<>();
            payload.put("correo", correo);
            payload.put("codigo", codigo);
            payload.put("nuevaPassword", nuevaPassword);
            webClient.post()
                    .uri("/usuarios/recuperar-password/cambiar")
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            System.out.println("Error cambiando contraseña: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Contrata un plan de suscripción para un usuario.
     * @param usuarioId ID del usuario.
     * @param nombrePlan Nombre del plan.
     * @param precio Precio del plan.
     * @return DTO de la suscripción.
     */
    public edu.backend_frontend_serviclick.dto.SuscripcionDTO contratarPlan(Long usuarioId, String nombrePlan,
            Double precio) {
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("usuarioId", usuarioId);
            payload.put("nombrePlan", nombrePlan);
            payload.put("precio", precio);

            return webClient.post()
                    .uri("/suscripciones/contratar")
                    .bodyValue(payload)
                    .retrieve()
                    .bodyToMono(edu.backend_frontend_serviclick.dto.SuscripcionDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error contratando plan: " + e.getMessage());
            return null;
        }
    }

    /**
     * Confirma la cuenta de usuario.
     * @param token Token de confirmación.
     * @return true si se confirmó.
     */
    public boolean confirmarCuenta(String token) {
        try {
            webClient.post()
                    .uri("/usuarios/confirmar")
                    .bodyValue(token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            System.out.println("Error confirmando cuenta: " + e.getMessage());
            return false;
        }
    }

    /**
     * Obtiene un servicio por ID (Método duplicado por conveniencia de nombrado).
     * @param id ID del servicio.
     * @return DTO del servicio.
     */
    public edu.backend_frontend_serviclick.dto.ServicioDTO obtenerServicioPorId(Long id) {
        try {
            return webClient.get()
                    .uri("/servicios/" + id)
                    .retrieve()
                    .bodyToMono(edu.backend_frontend_serviclick.dto.ServicioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error obteniendo servicio por ID: " + e.getMessage());
            return null;
        }
    }

    /**
     * Contrata un servicio específico.
     * @param usuarioId ID del usuario contratante.
     * @param servicioId ID del servicio.
     * @param precio Precio acordado.
     * @return true si la contratación fue exitosa.
     */
    public boolean contratarServicio(Long usuarioId, Long servicioId, Double precio) {
        try {
            java.util.Map<String, Object> payload = new java.util.HashMap<>();
            payload.put("usuarioId", usuarioId);
            payload.put("servicioId", servicioId);
            payload.put("precio", precio);

            webClient.post()
                    .uri("/contrataciones/crear")
                    .bodyValue(payload)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return true;
        } catch (Exception e) {
            System.out.println("Error contratando servicio: " + e.getMessage());
            return false;
        }
    }

    /**
     * Elimina un servicio.
     * @param servicioId ID del servicio.
     * @throws Exception Si hay error en la eliminación.
     */
    public void eliminarServicio(Long servicioId) throws Exception {
        try {
            webClient.delete()
                    .uri("/servicios/" + servicioId)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (org.springframework.web.reactive.function.client.WebClientResponseException e) {
            // Intentar extraer mensaje del backend
            throw new Exception(e.getResponseBodyAsString());
        } catch (Exception e) {
            System.out.println("Error eliminando servicio: " + e.getMessage());
            throw new Exception("Error al conectar con el servidor.");
        }
    }

    /**
     * Actualiza un servicio existente.
     * @param id ID del servicio.
     * @param servicio Datos actualizados.
     * @return DTO del servicio actualizado.
     */
    public edu.backend_frontend_serviclick.dto.ServicioDTO actualizarServicio(Long id,
            edu.backend_frontend_serviclick.dto.ServicioDTO servicio) {
        try {
            return webClient.put()
                    .uri("/servicios/" + id)
                    .bodyValue(servicio)
                    .retrieve()
                    .bodyToMono(edu.backend_frontend_serviclick.dto.ServicioDTO.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error actualizando servicio: " + e.getMessage());
            return null;
        }
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar.
     */
    public void eliminarUsuario(Long id) {
        try {
            webClient.delete()
                    .uri("/usuarios/" + id)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
        }
    }

    /**
     * Obtiene la factura en PDF de un usuario.
     * @param usuarioId ID del usuario.
     * @return Arreglo de bytes del PDF o null.
     */
    public byte[] obtenerFacturaPDF(Long usuarioId) {
        try {
            return webClient.get()
                    .uri("/reportes/factura/" + usuarioId)
                    .retrieve()
                    .bodyToMono(byte[].class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error descargando factura: " + e.getMessage());
            return null;
        }
    }
}