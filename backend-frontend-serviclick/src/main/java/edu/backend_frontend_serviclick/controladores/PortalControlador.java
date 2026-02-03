package edu.backend_frontend_serviclick.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalControlador {

    // Cuando entras a http://localhost:8081/, muestra index.html
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 2. Para ver los planes
    @GetMapping("/web/planes")
    public String verPlanes() {
        return "lista-planes";
    }

    // 3. Para la pantalla de pago
    @GetMapping("/web/pago")
    public String irAPagar(@RequestParam String plan, 
                           @RequestParam String precio, 
                           Model model) {
        model.addAttribute("planSeleccionado", plan);
        model.addAttribute("precioSeleccionado", precio);
        return "vista-pago"; 
    }

    @org.springframework.beans.factory.annotation.Autowired
    private edu.backend_frontend_serviclick.ApiCliente.ApiCliente apiCliente;

    @GetMapping("/web/servicios")
    public String listarServicios(@RequestParam(required = false) String keyword,
                                  @RequestParam(required = false) String categoria,
                                  @RequestParam(required = false) String orden,
                                  Model model) {
        java.util.List<edu.backend_frontend_serviclick.dto.ServicioDTO> servicios = apiCliente.listarServicios();
        
        // Filtrado en el cliente (Frontend/Controller)
        if (servicios != null) {
            java.util.stream.Stream<edu.backend_frontend_serviclick.dto.ServicioDTO> stream = servicios.stream();

            // Búsqueda inteligente: divide por espacios y busca que TODAS las palabras existan en título o descripción
            if (keyword != null && !keyword.isBlank()) {
                String[] words = keyword.toLowerCase().split("\\s+");
                stream = stream.filter(s -> {
                    String content = ((s.getTitulo() != null ? s.getTitulo() : "") + " " + 
                                      (s.getDescripcion() != null ? s.getDescripcion() : "")).toLowerCase();
                    for (String word : words) {
                        if (!content.contains(word)) return false;
                    }
                    return true;
                });
            }

            if (categoria != null && !categoria.isBlank() && !categoria.equals("Todas las categorías")) {
                stream = stream.filter(s -> s.getCategoria() != null && s.getCategoria().equalsIgnoreCase(categoria));
            }

            // Convertir a lista mutable para ordenar
            servicios = stream.collect(java.util.stream.Collectors.toList());

            // Lógica de ordenación
            if ("precio_asc".equals(orden)) {
                servicios.sort(java.util.Comparator.comparing(
                    edu.backend_frontend_serviclick.dto.ServicioDTO::getPrecioHora, 
                    java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder())
                ));
            } else if ("precio_desc".equals(orden)) {
                servicios.sort(java.util.Comparator.comparing(
                    edu.backend_frontend_serviclick.dto.ServicioDTO::getPrecioHora, 
                    java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())
                ));
            }
        }

        model.addAttribute("servicios", servicios);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", orden);
        return "listadoServicios";
    }

    @GetMapping("/registro")
    public String registro() {
        return "registro";
    }

    @GetMapping("/web/publicar")
    public String publicar(jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        return "publicar";
    }

    @org.springframework.web.bind.annotation.PostMapping("/web/publicar")
    public String procesarPublicacion(@RequestParam String titulo,
                                      @RequestParam String categoria,
                                      @RequestParam Double precioHora,
                                      @RequestParam String descripcion,
                                      @RequestParam(required = false) org.springframework.web.multipart.MultipartFile ficheroImagen,
                                      jakarta.servlet.http.HttpSession session) {
        
        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        edu.backend_frontend_serviclick.dto.UsuarioDTO usuario = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueado;
        
        edu.backend_frontend_serviclick.dto.ServicioDTO nuevoServicio = new edu.backend_frontend_serviclick.dto.ServicioDTO();
        nuevoServicio.setTitulo(titulo);
        nuevoServicio.setCategoria(categoria);
        nuevoServicio.setPrecioHora(precioHora);
        nuevoServicio.setDescripcion(descripcion);
        nuevoServicio.setProfesional(usuario);

        if (ficheroImagen != null && !ficheroImagen.isEmpty()) {
            try {
                String base64Image = "data:" + ficheroImagen.getContentType() + ";base64," + 
                    java.util.Base64.getEncoder().encodeToString(ficheroImagen.getBytes());
                nuevoServicio.setImagen(base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        apiCliente.crearServicio(nuevoServicio);
        
        return "redirect:/web/servicios";
    }

    @GetMapping("/recuperar-contrasena")
    public String recuperarContrasena() {
        return "recuperarContrasena";
    }

    @GetMapping("/web/perfil")
    public String perfilUsuario(org.springframework.ui.Model model, jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuarioLogueado);
        return "perfil";
    }

    @GetMapping("/logout")
    public String cerrarSesion(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/web/detalle-servicio")
    public String detalleServicio(@RequestParam Long id, Model model) {
        // Obtenemos todos y filtramos (o idealmente hacemos una llamada a la API por ID, pero por ahora filtramos)
        // La mejor opción es crear un método en ApiCliente.
        // Pero como ya existe el endpoint GET /api/servicios (y quizás no por ID específico accesible públicamente igual),
        // vamos a añadir buscarServicioPorId en ApiCliente.
        
        // Asumimos que implementaremos buscarServicioPorId en ApiCliente
        // Si no, podríamos filtrar de la lista completa (menos eficiente)
        
        // Opción temporal: Filtrar de la lista completa
        /*
        java.util.List<edu.backend_frontend_serviclick.dto.ServicioDTO> servicios = apiCliente.listarServicios();
        edu.backend_frontend_serviclick.dto.ServicioDTO servicioEncontrado = servicios.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
        */
        
        // Opción Correcta: Llamar a la API
        // Necesitamos implementar el método en ApiCliente
        // Por ahora, para no bloquear, si no tienes el método, usa el filtrado.
        // Pero como soy el asistente, voy a implementar el método en ApiCliente en el siguiente paso.
        // Aquí asumiré que existe.
        
        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = apiCliente.buscarServicioPorId(id);
        model.addAttribute("servicio", servicio);
        
        return "detalleServicio";
    }
}
