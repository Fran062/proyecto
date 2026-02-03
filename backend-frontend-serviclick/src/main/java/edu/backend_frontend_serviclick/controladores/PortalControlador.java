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
    public String verPlanes(Model model, jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj != null) {
            edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;
            // Refrescar datos
            edu.backend_frontend_serviclick.dto.UsuarioDTO fresco = apiCliente.buscarUsuarioPorId(u.getId());
            if (fresco != null) {
                session.setAttribute("usuarioLogueado", fresco);
                model.addAttribute("usuario", fresco);
            } else {
                model.addAttribute("usuario", u);
            }
        }
        return "lista-planes";
    }

    // Endpoint para procesar el pago (AJAX)
    @org.springframework.web.bind.annotation.PostMapping("/web/pago/procesar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Void> procesarPago(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> payload,
            jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return org.springframework.http.ResponseEntity.status(401).build();
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        try {
            String plan = payload.get("plan");
            Double precio = Double.parseDouble(payload.get("precio"));

            edu.backend_frontend_serviclick.dto.SuscripcionDTO sub = apiCliente.contratarPlan(u.getId(), plan, precio);

            if (sub != null) {
                // Actualizar usuario en sesión
                u.setSuscripcion(sub);
                // Refrescar completo por si acaso
                edu.backend_frontend_serviclick.dto.UsuarioDTO fresco = apiCliente.buscarUsuarioPorId(u.getId());
                session.setAttribute("usuarioLogueado", fresco != null ? fresco : u);
                return org.springframework.http.ResponseEntity.ok().build();
            } else {
                return org.springframework.http.ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().build();
        }
    }

    // 3. Para la pantalla de pago
    @GetMapping("/web/pago")
    public String irAPagar(@RequestParam String plan,
            @RequestParam String precio,
            Model model,
            jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
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

            // Búsqueda inteligente: divide por espacios y busca que TODAS las palabras
            // existan en título o descripción
            if (keyword != null && !keyword.isBlank()) {
                String[] words = keyword.toLowerCase().split("\\s+");
                stream = stream.filter(s -> {
                    String content = ((s.getTitulo() != null ? s.getTitulo() : "") + " " +
                            (s.getDescripcion() != null ? s.getDescripcion() : "")).toLowerCase();
                    for (String word : words) {
                        if (!content.contains(word))
                            return false;
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
                        java.util.Comparator.nullsLast(java.util.Comparator.naturalOrder())));
            } else if ("precio_desc".equals(orden)) {
                servicios.sort(java.util.Comparator.comparing(
                        edu.backend_frontend_serviclick.dto.ServicioDTO::getPrecioHora,
                        java.util.Comparator.nullsLast(java.util.Comparator.reverseOrder())));
            }
        }

        model.addAttribute("servicios", servicios);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("ordenSeleccionado", orden);
        return "listadoServicios";
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
        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = apiCliente.buscarServicioPorId(id);
        model.addAttribute("servicio", servicio);

        if (servicio != null) {
            java.util.List<edu.backend_frontend_serviclick.dto.ResenaDTO> resenas = apiCliente
                    .obtenerResenasPorServicio(id);
            Double promedio = apiCliente.obtenerPromedioServicio(id);

            model.addAttribute("resenas", resenas);
            model.addAttribute("promedio", promedio);
        }

        return "detalleServicio";
    }

    @org.springframework.web.bind.annotation.PostMapping("/web/resenar")
    public String publicarResena(@RequestParam Long servicioId,
            @RequestParam Integer calificacion,
            @RequestParam String comentario,
            jakarta.servlet.http.HttpSession session) {

        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        edu.backend_frontend_serviclick.dto.UsuarioDTO usuario = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueado;

        // We need the service object to send it to the API (or at least the ID if the
        // DTO structure allows it)
        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = new edu.backend_frontend_serviclick.dto.ServicioDTO();
        servicio.setId(servicioId);

        edu.backend_frontend_serviclick.dto.ResenaDTO resena = new edu.backend_frontend_serviclick.dto.ResenaDTO();
        resena.setCalificacion(calificacion);
        resena.setComentario(comentario);
        resena.setServicio(servicio);
        resena.setUsuario(usuario);

        apiCliente.crearResena(resena);

        // Redirect back to the service details
        return "redirect:/web/detalle-servicio?id=" + servicioId;
    }

    @org.springframework.web.bind.annotation.PostMapping("/web/resenar/eliminar")
    public String eliminarResena(@RequestParam Long resenaId, @RequestParam Long servicioId,
            jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        apiCliente.eliminarResena(resenaId);
        return "redirect:/web/detalle-servicio?id=" + servicioId;
    }

    @org.springframework.web.bind.annotation.PostMapping("/web/perfil/guardar")
    public String guardarPerfil(
            @org.springframework.web.bind.annotation.ModelAttribute("usuario") edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioDTO,
            @RequestParam(required = false) String nuevaPassword,
            jakarta.servlet.http.HttpSession session) {

        // Ensure user is logged in
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioLogueado = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        // Update fields (except email which is read-only usually, or ID)
        usuarioLogueado.setNombreCompleto(usuarioDTO.getNombreCompleto());
        usuarioLogueado.setTelefono(usuarioDTO.getTelefono());

        // Pass password only if it's set
        if (nuevaPassword != null && !nuevaPassword.isBlank()) {
            usuarioLogueado.setPassword(nuevaPassword);
        }

        // Call API to update user
        apiCliente.actualizarUsuario(usuarioLogueado.getId(), usuarioLogueado);

        // Update session
        session.setAttribute("usuarioLogueado", usuarioLogueado);

        return "redirect:/web/perfil?exito=true";
    }

    // Endpoint para solicitar el código (AJAX)
    @org.springframework.web.bind.annotation.PostMapping("/api/recuperar-password/solicitar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Void> solicitarCodigo(
            @org.springframework.web.bind.annotation.RequestBody String correo) {
        try {
            apiCliente.solicitarRecuperacionPass(correo);
            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().build();
        }
    }

    // Endpoint para verificar el código (AJAX)
    @org.springframework.web.bind.annotation.PostMapping("/api/recuperar-password/verificar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Boolean> verificarCodigo(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> payload) {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        boolean valido = apiCliente.verificarCodigoRecuperacion(correo, codigo);
        return org.springframework.http.ResponseEntity.ok(valido);
    }

    // Endpoint para cambiar la contraseña (AJAX)
    @org.springframework.web.bind.annotation.PostMapping("/api/recuperar-password/cambiar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Void> cambiarPassword(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> payload) {
        try {
            apiCliente.cambiarPasswordConCodigo(payload.get("correo"), payload.get("codigo"),
                    payload.get("nuevaPassword"));
            return org.springframework.http.ResponseEntity.ok().build();
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().build();
        }
    }
}
