/* --- main.js --- */

// Mensaje cuando intentan usar funciones que requieren backend
function showBackendAlert() {
    alert('Esta función (Publicar/Contratar) requiere conexión a base de datos y backend.');
}

// Simulación de Login
function handleLogin(event) {
    event.preventDefault(); // Evita que la página se recargue
    const email = document.querySelector('input[type="email"]').value;
    const password = document.querySelector('input[type="password"]').value;

    if(email && password) {
        alert('Login simulado correctamente. Redirigiendo al inicio...');
        window.location.href = 'index.html';
    } else {
        alert('Por favor completa los campos');
    }
}

// Simulación de contacto
function contactProfessional() {
    alert('Proximamente chat o pasarela de pago');
}


// Función para procesar el formulario de publicar
function handlePublish(event) {
    event.preventDefault(); // Evita recargar la página
    
    // Simulación de carga (Feedback al usuario)
    const btn = event.target.querySelector('button[type="submit"]');
    const textoOriginal = btn.innerText;
    
    btn.disabled = true;
    btn.innerText = "Publicando...";
    btn.style.opacity = "0.7";

    // Simulamos un retraso de 1.5 segundos (como si guardara en BD)
    setTimeout(() => {
        alert("¡Tu servicio se ha publicado correctamente!");
        
        // Redirigir al listado para ver los cambios (simulado)
        window.location.href = 'listadoServicios.html';
    }, 1500);
}