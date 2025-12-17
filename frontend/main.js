/* --- main.js ACTUALIZADO --- */

const CLAVE_SESION = 'usuarioLogueado';

document.addEventListener('DOMContentLoaded', () => {
    actualizarBotonSesion();
});

// --- 1. ACTUALIZACIÓN DEL BLOQUEO (HARD WALL) ---
document.addEventListener('click', (event) => {
    const estoyEnLogin = window.location.href.includes('login.html');
    const estoyEnRegistro = window.location.href.includes('registro.html');

    const estoyEnRecuperacion = window.location.href.includes('recuperarContrasena.html'); 
    
    const usuarioEstaLogueado = localStorage.getItem(CLAVE_SESION) === 'true';

    // Añadimos !estoyEnRecuperacion a la condición
    if (!usuarioEstaLogueado && !estoyEnLogin && !estoyEnRegistro && !estoyEnRecuperacion) {
        
        // Dejamos pasar clics si van hacia estas páginas
        const target = event.target.closest('a');
        if (target && (
            target.href.includes('login.html') || 
            target.href.includes('registro.html') || 
            target.href.includes('recuperarContrasena.html')
        )) {
            return;
        }

        event.preventDefault();
        event.stopPropagation();
        alert('Acceso restringido: Debes iniciar sesión.');
        window.location.href = 'login.html';
    }
}, true);


function handleRegister(event) {
    event.preventDefault();

    // Capturar datos
    const pass = document.getElementById('reg-pass').value;
    const confirmPass = document.getElementById('reg-pass-confirm').value;
    const nombre = document.getElementById('reg-nombre').value;

    // Validación básica
    if (pass !== confirmPass) {
        alert("Las contraseñas no coinciden. Inténtalo de nuevo.");
        return;
    }

    if (pass.length < 4) {
        alert("La contraseña es muy corta.");
        return;
    }

    // Simulación de éxito
    // Aquí podríamos guardar el nombre en localStorage para usarlo en el perfil
    localStorage.setItem('nombreUsuarioTemp', nombre); 

    alert("¡Cuenta creada con éxito! Ahora por favor inicia sesión.");
    window.location.href = 'login.html';
}


function handleLogin(event) {
    event.preventDefault(); 
    const emailInput = document.querySelector('input[type="email"]');
    const passwordInput = document.querySelector('input[type="password"]');
    
    if(emailInput.value.length > 0 && passwordInput.value.length > 0) {
        localStorage.setItem(CLAVE_SESION, 'true');
        alert('Login correcto. Bienvenido.');
        window.location.href = 'index.html';
    } else {
        alert('Por favor, escribe un correo y una contraseña.');
    }
}

function cerrarSesion() {
    localStorage.removeItem(CLAVE_SESION);
    localStorage.removeItem('nombreUsuarioTemp'); // Limpiamos datos temp
    alert('Has cerrado sesión.');
    window.location.href = 'login.html';
}

function actualizarBotonSesion() {
    const container = document.querySelector('.auth-buttons');
    if (!container) return;

    if (localStorage.getItem(CLAVE_SESION) === 'true') {
        container.innerHTML = `
            <div class="user-account-badge" onclick="window.location.href='perfil.html'" title="Ir a mi perfil">
                <i class="fas fa-user"></i>
            </div>
        `;
    } else {
        // Mostrar botones de Login y Registro si no hay sesión
        container.innerHTML = `
            <a href="login.html" class="btn-primary" style="margin-right: 10px;">Entrar</a>
            <a href="registro.html" class="btn-outline">Registrarse</a>
        `;
    }
}

function guardarPerfil(event) {
    event.preventDefault();
    const btn = event.target.querySelector('button[type="submit"]');
    const textoOriginal = btn.innerText;
    btn.innerText = "Guardando...";
    btn.disabled = true;
    setTimeout(() => {
        btn.innerText = textoOriginal;
        btn.disabled = false;
        alert("¡Datos actualizados correctamente!");
    }, 1000);
}

function showBackendAlert() {
    alert('Función disponible solo para usuarios registrados.');
}

function contactProfessional() {
     alert('Abriendo pasarela de pago...');
}

function handlePublish(event) {
    event.preventDefault();
    const btn = event.target.querySelector('button[type="submit"]');
    btn.disabled = true;
    btn.innerText = "Publicando...";
    setTimeout(() => {
        alert("¡Tu servicio se ha publicado correctamente!");
        window.location.href = 'listadoServicios.html';
    }, 1500);
}

// Variable para guardar el código correcto temporalmente
let codigoCorrectoGenerado = null;

// PASO 1: Enviar Código
function enviarCodigo() {
    const emailInput = document.getElementById('rec-email');
    
    if (!emailInput.value || !emailInput.value.includes('@')) {
        alert("Por favor, introduce un correo válido.");
        return;
    }

    // Simulamos carga
    const btn = document.querySelector('#step-email button');
    const textoOriginal = btn.innerText;
    btn.innerText = "Enviando...";
    btn.disabled = true;

    setTimeout(() => {
        // Generamos un número aleatorio entre 1000 y 9999
        codigoCorrectoGenerado = Math.floor(1000 + Math.random() * 9000);
        
        // SIMULACIÓN DE EMAIL: Mostramos el código en un alert para que el usuario lo sepa
        alert(`\n\nHola,\nTu código de recuperación para ServiClicks es: ${codigoCorrectoGenerado}\n\n(Cópialo para el siguiente paso)`);

        // Cambiamos visualmente al Paso 2
        document.getElementById('step-email').style.display = 'none';
        document.getElementById('step-code').style.display = 'block';
        
        // Actualizamos textos de cabecera
        document.getElementById('titulo-pagina').innerText = "Verificar Código";
        document.getElementById('subtitulo-pagina').innerText = "Introduce el código enviado a " + emailInput.value;

        btn.innerText = textoOriginal;
        btn.disabled = false;
    }, 1500);
}

// PASO 2: Verificar Código
function verificarCodigo() {
    const codigoUsuario = document.getElementById('rec-code-input').value;

    if (parseInt(codigoUsuario) === codigoCorrectoGenerado) {
        // Código Correcto -> Pasamos al Paso 3
        alert("¡Código verificado correctamente!");

        document.getElementById('step-code').style.display = 'none';
        document.getElementById('step-password').style.display = 'block';

        document.getElementById('titulo-pagina').innerText = "Nueva Contraseña";
        document.getElementById('subtitulo-pagina').innerText = "Escribe tu nueva clave de acceso.";
    } else {
        // Código Incorrecto
        alert("El código introducido es INCORRECTO. Inténtalo de nuevo.");
    }
}

// PASO 3: Guardar Nueva Contraseña
function guardarNuevaPassword() {
    const pass1 = document.getElementById('rec-new-pass').value;
    const pass2 = document.getElementById('rec-confirm-pass').value;

    if (pass1.length < 4) {
        alert("La contraseña es muy corta.");
        return;
    }

    if (pass1 !== pass2) {
        alert("Las contraseñas no coinciden.");
        return;
    }

    // Simulación de guardado
    alert("¡Contraseña restablecida con éxito!\nAhora puedes iniciar sesión con tu nueva clave.");
    window.location.href = 'login.html';
}

// Función auxiliar para volver atrás si se equivocó de email
function reiniciarRecuperacion() {
    document.getElementById('step-code').style.display = 'none';
    document.getElementById('step-email').style.display = 'block';
    document.getElementById('titulo-pagina').innerText = "Recuperar Contraseña";
    document.getElementById('subtitulo-pagina').innerText = "Introduce tu correo para buscar tu cuenta.";
    codigoCorrectoGenerado = null;
}