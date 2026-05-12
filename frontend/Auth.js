const URL_BASE = "http://localhost:8080/api/auth";

// ==================== CARGAR TECNOLOGÍAS ====================

/**
 * Carga las tecnologías disponibles desde el backend y las muestra
 * como badges seleccionables en el formulario de registro.
 */
async function cargarTecnologias() {
    const selector = document.getElementById('tech-selector');
    if (!selector) return;

    try {
        const res = await fetch("http://localhost:8080/api/tecnologias");
        const tecnologias = await res.json();

        selector.innerHTML = '';

        tecnologias.forEach(tech => {
            // Checkbox oculto
            const checkbox = document.createElement('input');
            checkbox.type = 'checkbox';
            checkbox.className = 'tech-checkbox';
            checkbox.id = `tech-${tech.id}`;
            checkbox.value = tech.id;
            checkbox.name = 'tecnologias';

            // Label visible (el badge clicable)
            const label = document.createElement('label');
            label.className = 'tech-label';
            label.htmlFor = `tech-${tech.id}`;
            label.setAttribute('data-tech', tech.nombre);
            label.textContent = tech.nombre;

            selector.appendChild(checkbox);
            selector.appendChild(label);
        });

    } catch (error) {
        console.error("Error cargando tecnologías:", error);
        selector.innerHTML = '<p class="tech-loading">Error al cargar tecnologías</p>';
    }
}

// ==================== REGISTRO ====================

async function ejecutarRegistro(event) {
    event.preventDefault(); // Detiene el envío tradicional del formulario

    // Recoger las tecnologías seleccionadas
    const techCheckboxes = document.querySelectorAll('.tech-checkbox:checked');
    const tecnologias = Array.from(techCheckboxes).map(cb => parseInt(cb.value));

    const datos = {
        nombre: document.getElementById('reg-nombre').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-pass').value,
        ciudad: document.getElementById('reg-ciudad').value,
        descripcion: document.getElementById('reg-descripcion').value,
        tecnologias: tecnologias
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

// ==================== LOGIN ====================

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
            localStorage.setItem('lovecode_user_id', info.id);
            window.location.href = "dashboard.html";
        } else {
            alert("Acceso denegado: " + info.error);
        }
    } catch (error) {
        console.error("Error:", error);
    }
}