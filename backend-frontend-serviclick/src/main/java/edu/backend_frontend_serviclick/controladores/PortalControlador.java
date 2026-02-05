package edu.backend_frontend_serviclick.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PortalControlador {

    /**
     * Muestra la página de inicio.
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 2. Para ver los planes
    /**
     * Muestra la página de planes de suscripción.
     * @param model Modelo para la vista.
     * @param session Sesión HTTP.
     * @return Vista lista-planes.
     */
    @GetMapping("/web/planes")
    public String verPlanes(Model model, jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj != null) {
            edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

            if (!"CLIENTE".equalsIgnoreCase(u.getRol()) || !"ADMIN".equalsIgnoreCase(u.getRol())) {
                return "redirect:/";
            }

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

    /**
     * Procesa el pago de una suscripción.
     * @param payload Datos del pago.
     * @param session Sesión HTTP.
     * @return 200 OK si correcto.
     */
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
    /**
     * Muestra la página de pago para un plan específico.
     * @param plan Nombre del plan.
     * @param precio Precio del plan.
     * @return Vista vista-pago.
     */
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

    /**
     * Lista los servicios disponibles con filtros.
     * @param keyword Palabra clave de búsqueda.
     * @param categoria Categoría filtro.
     * @param orden Ordenamiento.
     * @param model Modelo.
     * @return Vista listadoServicios.
     */
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

    /**
     * Muestra el formulario para publicar un nuevo servicio.
     */
    @GetMapping("/web/publicar")
    public String publicar(jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO usuario = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;
        if (!"PROFESIONAL".equalsIgnoreCase(usuario.getRol()) && !"ADMIN".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/";
        }
        return "publicar";
    }

    /**
     * Procesa la publicación de un nuevo servicio.
     * @param titulo Título.
     * @param categoria Categoría.
     * @param precioHora Precio.
     * @param descripcion Descripción.
     * @param ficheroImagen Archivo de imagen opcional.
     * @param session Sesión.
     * @return Redirección a listado de servicios.
     */
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
        if (!"PROFESIONAL".equalsIgnoreCase(usuario.getRol()) && !"ADMIN".equalsIgnoreCase(usuario.getRol())) {
            return "redirect:/";
        }

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

        return "redirect:/web/servicios?publicado=true";
    }

    /**
     * Muestra la vista de recuperación de contraseña.
     */
    @GetMapping("/recuperar-contrasena")
    public String recuperarContrasena() {
        return "recuperarContrasena";
    }

    /**
     * Muestra el perfil del usuario logueado.
     */
    @GetMapping("/web/perfil")
    public String perfilUsuario(org.springframework.ui.Model model, jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }
        model.addAttribute("usuario", usuarioLogueado);
        return "perfil";
    }

    /**
     * Cierra la sesión del usuario.
     */
    @GetMapping("/logout")
    public String cerrarSesion(jakarta.servlet.http.HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout=true";
    }

    /**
     * Muestra el detalle completo de un servicio.
     * @param id ID del servicio.
     */
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

    /**
     * Publica una reseña para un servicio.
     */
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

        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = new edu.backend_frontend_serviclick.dto.ServicioDTO();
        servicio.setId(servicioId);

        edu.backend_frontend_serviclick.dto.ResenaDTO resena = new edu.backend_frontend_serviclick.dto.ResenaDTO();
        resena.setCalificacion(calificacion);
        resena.setComentario(comentario);
        resena.setServicio(servicio);
        resena.setUsuario(usuario);

        apiCliente.crearResena(resena);

        return "redirect:/web/detalle-servicio?id=" + servicioId + "&resenaExito=true";
    }

    /**
     * Elimina una reseña propia.
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/resenar/eliminar")
    public String eliminarResena(@RequestParam Long resenaId, @RequestParam Long servicioId,
            jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        apiCliente.eliminarResena(resenaId);
        return "redirect:/web/detalle-servicio?id=" + servicioId + "&resenaEliminada=true";
    }

    /**
     * Guarda los cambios del perfil de usuario.
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/perfil/guardar")
    public String guardarPerfil(
            @org.springframework.web.bind.annotation.ModelAttribute("usuario") edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioDTO,
            @RequestParam(required = false) String nuevaPassword,
            jakarta.servlet.http.HttpSession session) {

        // Asegurar que el usuario está logueado
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioLogueado = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        // Actualizar campos (excepto email que suele ser de solo lectura, o ID)
        usuarioLogueado.setNombreCompleto(usuarioDTO.getNombreCompleto());
        usuarioLogueado.setTelefono(usuarioDTO.getTelefono());

        // Pasar contraseña solo si está establecida
        if (nuevaPassword != null && !nuevaPassword.isBlank()) {
            usuarioLogueado.setPassword(nuevaPassword);
        }

        // Llamada a la API para actualizar el usuario
        apiCliente.actualizarUsuario(usuarioLogueado.getId(), usuarioLogueado);

        // Actualizar sesión
        session.setAttribute("usuarioLogueado", usuarioLogueado);

        return "redirect:/web/perfil?exito=true";
    }

    /**
     * Solicita código de recuperación (AJAX).
     */
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

    /**
     * Confirma la cuenta con el token.
     */
    @GetMapping("/confirmar")
    public String confirmarCuenta(@RequestParam String token) {
        boolean exito = apiCliente.confirmarCuenta(token);
        if (exito) {
            return "redirect:/login?verificado=true";
        } else {
            return "redirect:/login?errorVerificacion=true";
        }
    }

    /**
     * Verifica código de recuperación (AJAX).
     */
    @org.springframework.web.bind.annotation.PostMapping("/api/recuperar-password/verificar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Boolean> verificarCodigo(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, String> payload) {
        String correo = payload.get("correo");
        String codigo = payload.get("codigo");
        boolean valido = apiCliente.verificarCodigoRecuperacion(correo, codigo);
        return org.springframework.http.ResponseEntity.ok(valido);
    }

    /**
     * Cambia la contraseña (AJAX).
     */
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

    /**
     * Muestra la vista de pago de un servicio.
     */
    @GetMapping("/web/pago-servicio")
    public String mostrarPagoServicio(@RequestParam Long servicioId,
            @RequestParam Double precio,
            Model model,
            jakarta.servlet.http.HttpSession session) {
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }

        // Obtener detalles del servicio
        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = apiCliente.obtenerServicioPorId(servicioId);
        model.addAttribute("servicio", servicio);
        model.addAttribute("servicioId", servicioId);
        model.addAttribute("precio", precio);
        return "pago-servicio";
    }

    /**
     * Procesa el pago de un servicio contratado (AJAX).
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/pago-servicio/procesar")
    @org.springframework.web.bind.annotation.ResponseBody
    public org.springframework.http.ResponseEntity<Void> procesarPagoServicio(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, Object> payload,
            jakarta.servlet.http.HttpSession session) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return org.springframework.http.ResponseEntity.status(401).build();
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        try {
            Long servicioId = Long.valueOf(payload.get("servicioId").toString());
            Double precio = Double.valueOf(payload.get("precio").toString());

            // Llamar al API para crear la contratación
            boolean exito = apiCliente.contratarServicio(u.getId(), servicioId, precio);

            if (exito) {
                return org.springframework.http.ResponseEntity.ok().build();
            } else {
                return org.springframework.http.ResponseEntity.badRequest().build();
            }
        } catch (Exception e) {
            return org.springframework.http.ResponseEntity.badRequest().build();
        }
    }


    /**
     * Muestra el formulario para editar un servicio existente.
     */
    @GetMapping("/web/editar-servicio")
    public String editarServicio(@RequestParam Long id, Model model, jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        edu.backend_frontend_serviclick.dto.ServicioDTO servicio = apiCliente.buscarServicioPorId(id);
        if (servicio == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "Servicio no encontrado.");
            return "redirect:/web/servicios";
        }

        // Verificar permisos: Solo el dueño o el admin pueden editar
        if (servicio.getProfesional() == null || (!servicio.getProfesional().getId().equals(u.getId())
                && !u.getRol().toString().equals("ADMIN"))) {
            redirectAttributes.addFlashAttribute("mensajeError", "No tienes permisos para editar este servicio.");
            return "redirect:/web/servicios";
        }

        model.addAttribute("servicio", servicio);
        return "publicar"; // Reutilizamos la vista
    }

    /**
     * Procesa la edición de un servicio.
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/editar-servicio")
    public String procesarEdicionServicio(@RequestParam Long id,
            @RequestParam String titulo,
            @RequestParam String categoria,
            @RequestParam Double precioHora,
            @RequestParam String descripcion,
            @RequestParam(required = false) org.springframework.web.multipart.MultipartFile ficheroImagen,
            jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Object usuarioLogueado = session.getAttribute("usuarioLogueado");
        if (usuarioLogueado == null) {
            return "redirect:/login";
        }

        // Recuperar servicio actual
        edu.backend_frontend_serviclick.dto.ServicioDTO servicioActual = apiCliente.buscarServicioPorId(id);
        if (servicioActual == null) {
            redirectAttributes.addFlashAttribute("mensajeError", "El servicio no existe o fue eliminado.");
            return "redirect:/web/servicios";
        }

        // Actualizar datos
        servicioActual.setTitulo(titulo);
        servicioActual.setCategoria(categoria);
        servicioActual.setPrecioHora(precioHora);
        servicioActual.setDescripcion(descripcion);

        if (ficheroImagen != null && !ficheroImagen.isEmpty()) {
            try {
                String base64Image = "data:" + ficheroImagen.getContentType() + ";base64," +
                        java.util.Base64.getEncoder().encodeToString(ficheroImagen.getBytes());
                servicioActual.setImagen(base64Image);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        apiCliente.actualizarServicio(id, servicioActual);

        return "redirect:/web/detalle-servicio?id=" + id + "&editado=true";
    }
    /**
     * Busca un usuario por correo para eliminar (Solo ADMIN).
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/admin/buscar-usuario-borrar")
    public String buscarUsuarioParaBorrar(@RequestParam String emailBusqueda,
            Model model,
            jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO admin = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        // Security check
        if (!"ADMIN".equalsIgnoreCase(admin.getRol())) {
            return "redirect:/";
        }

        // Add admin info to model (needed for the layout)
        model.addAttribute("usuario", admin);

        edu.backend_frontend_serviclick.dto.UsuarioDTO usuarioEncontrado = apiCliente
                .buscarUsuarioPorCorreo(emailBusqueda);

        if (usuarioEncontrado != null) {
            model.addAttribute("usuarioParaBorrar", usuarioEncontrado);
        } else {
            model.addAttribute("errorBusqueda", "No se encontró ningún usuario con ese correo.");
        }

        return "perfil";
    }

    /**
     * Confirma y ejecuta el borrado de un usuario (Solo ADMIN).
     */
    @org.springframework.web.bind.annotation.PostMapping("/web/admin/borrar-usuario-confirmado")
    public String borrarUsuarioConfirmado(@RequestParam Long idUsuarioParaBorrar,
            @RequestParam String confirmacion,
            jakarta.servlet.http.HttpSession session,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {

        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return "redirect:/login";
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO admin = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        if (!"ADMIN".equalsIgnoreCase(admin.getRol())) {
            return "redirect:/";
        }

        if (!"borrar".equalsIgnoreCase(confirmacion)) {
            redirectAttributes.addFlashAttribute("errorBorrado", "La palabra de confirmación no es correcta.");
            return "redirect:/web/perfil";
        }

        // Prevent self-deletion
        if (admin.getId().equals(idUsuarioParaBorrar)) {
            redirectAttributes.addFlashAttribute("errorBorrado", "No puedes borrarte a ti mismo.");
            return "redirect:/web/perfil";
        }

        // Delete
        apiCliente.eliminarUsuario(idUsuarioParaBorrar);

        redirectAttributes.addFlashAttribute("mensajeExitoAdmin", "Usuario eliminado correctamente.");
        return "redirect:/web/perfil?exitoBorrado=true";
    }

    /**
     * Descarga la factura en PDF de un usuario (Proxy a API).
     */
    @GetMapping("/web/reportes/factura/{usuarioId}")
    public org.springframework.http.ResponseEntity<byte[]> descargarFactura(
            @org.springframework.web.bind.annotation.PathVariable Long usuarioId,
            jakarta.servlet.http.HttpSession session) {

        Object usuarioLogueadoObj = session.getAttribute("usuarioLogueado");
        if (usuarioLogueadoObj == null) {
            return org.springframework.http.ResponseEntity.status(401).build();
        }
        edu.backend_frontend_serviclick.dto.UsuarioDTO u = (edu.backend_frontend_serviclick.dto.UsuarioDTO) usuarioLogueadoObj;

        // Security check: Only allow downloading own invoice or Admin
        if (!u.getId().equals(usuarioId) && !"ADMIN".equalsIgnoreCase(u.getRol())) {
            return org.springframework.http.ResponseEntity.status(403).build();
        }

        byte[] pdfBytes = apiCliente.obtenerFacturaPDF(usuarioId);

        if (pdfBytes != null) {
            return org.springframework.http.ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=factura_" + usuarioId + ".pdf")
                    .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                    .body(pdfBytes);
        } else {
            return org.springframework.http.ResponseEntity.notFound().build();
        }
    }
}
