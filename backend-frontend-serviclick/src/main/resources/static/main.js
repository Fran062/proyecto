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

// Configuración Global de Toast (si Swal está cargado)
if (typeof Swal !== 'undefined') {
  window.Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 3000,
    timerProgressBar: false
  });
}

function showBackendAlert() {
  if (typeof window.Toast !== 'undefined') {
    window.Toast.fire({
      icon: 'warning',
      title: 'Acceso Restringido',
      text: 'Esta función requiere estar registrado.'
    });
  } else {
    alert("Esta función requiere estar registrado.");
  }
}

function contactProfessional() {
  if (typeof window.Toast !== 'undefined') {
    window.Toast.fire({
      icon: 'info',
      title: 'Redirigiendo...',
      text: 'Conectando con la pasarela de pago.'
    });
  } else {
    alert("Redirigiendo a la pasarela de pago...");
  }
}
