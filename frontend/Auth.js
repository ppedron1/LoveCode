const URL_BASE = "http://localhost:8080/api/auth";

async function ejecutarRegistro(event) {
    event.preventDefault(); // Detiene el envío tradicional del formulario

    const datos = {
        nombre: document.getElementById('reg-nombre').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-pass').value,
        ciudad: document.getElementById('reg-ciudad').value,
        descripcion: document.getElementById('reg-descripcion').value,
        tecnologiasIds: Array.from(document.querySelectorAll('input[name="tech"]:checked')).map(cb => parseInt(cb.value))
    };

    try {
        const res = await fetch(`${URL_BASE}/registro`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(datos)
        });

        const info = await res.json();
        
        if (res.ok) {
            alert("¡Éxito!: " + info.mensaje);
            window.location.href = "login.html";
        } else {
            alert("Error: " + (info.error || "No se pudo registrar"));
        }
    } catch (error) {
        console.error("Error de red:", error);
        alert("No se pudo conectar con el servidor Java");
    }
}

async function ejecutarLogin(event) {
    event.preventDefault();

    const creds = {
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-pass').value
    };

    try {
        const res = await fetch(`${URL_BASE}/login`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(creds)
        });

        const info = await res.json();

        if (res.ok) {
            alert("Bienvenido, " + info.nombre);
            localStorage.setItem('lovecode_user', info.nombre);
            localStorage.setItem('lovecode_user_id', info.id); // Guardamos el ID para los likes
            window.location.href = "dashboard.html";
        } else {
            alert("Acceso denegado: " + info.error);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}

// --- Gestión de Tecnologías en Registro ---

async function cargarTecnologiasEnRegistro() {
    const container = document.getElementById('tech-container');
    if (!container) return; // No estamos en la página de registro

    try {
        const res = await fetch("http://localhost:8080/api/tecnologias");
        const tecnologias = await res.json();

        container.innerHTML = '';
        tecnologias.forEach(t => {
            const label = document.createElement('label');
            label.className = 'tech-item';
            label.innerHTML = `
                <input type="checkbox" name="tech" value="${t.id}">
                <span>${t.nombre}</span>
            `;
            container.appendChild(label);
        });

    } catch (error) {
        console.error("Error cargando tecnologías:", error);
        container.innerHTML = '<p style="color: #ff6b6b;">Error al cargar tecnologías</p>';
    }
}

// Ejecutar si estamos en registro
if (document.getElementById('tech-container')) {
    cargarTecnologiasEnRegistro();
}