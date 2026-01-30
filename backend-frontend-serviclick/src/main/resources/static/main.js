// main.js - Versión limpia para Spring Boot

document.addEventListener("DOMContentLoaded", () => {
  console.log("Frontend cargado.");

  // Inicializar buscador si existe
  const inputBuscador = document.getElementById("buscador-servicios");
  if (inputBuscador) {
    cargarServicios(); // Llama a tu función de carga (asegúrate de que esa función apunte a tu API)
  }
});

// --- FUNCIONES VISUALES ---

function showBackendAlert() {
  alert("Esta función requiere estar registrado.");
}

function contactProfessional() {
  alert("Redirigiendo a la pasarela de pago...");
}

// Puedes mantener tus funciones de cargarServicios() y mostrarServiciosEnHTML()
// pero asegúrate de que la URL del fetch apunte a tu API real (http://localhost:8080/api/servicios)
