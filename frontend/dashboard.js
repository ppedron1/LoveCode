const API_URL = "http://localhost:8080/api/usuarios";

// Comprobar si el usuario está logueado
const usuarioLogueado = localStorage.getItem('lovecode_user');
if (!usuarioLogueado) {
    window.location.href = "login.html"; // Si no está logueado, echarlo al login
} else {
    document.getElementById('user-greeting').textContent = "Hola, " + usuarioLogueado;
}

function cerrarSesion() {
    localStorage.removeItem('lovecode_user');
    window.location.href = "index.html";
}

async function cargarPerfiles() {
    const grid = document.getElementById('profiles-grid');

    try {
        const res = await fetch(API_URL);
        const usuarios = await res.json();

        if (usuarios.length === 0) {
            grid.innerHTML = '<p style="color: #aaa; text-align: center; grid-column: 1/-1;">No hay perfiles registrados aún.</p>';
            return;
        }

        grid.innerHTML = '';

        // En el dashboard cargamos TODOS los usuarios
        usuarios.forEach(u => {
            // No mostrar el perfil del propio usuario logueado
            if (u.nombre === usuarioLogueado) return;

            const card = document.createElement('div');
            card.className = 'profile-card';
            card.innerHTML = `
                <div class="profile-info">
                    <h3>${u.nombre} <span class="status online"></span></h3>
                    <p class="role">📍 ${u.ciudad || 'Sin ciudad'}</p>
                    <p class="bio">"${u.descripcion || 'Sin descripción'}"</p>
                </div>
                <div class="card-actions">
                    <button class="action-btn nope" aria-label="Rechazar">✗</button>
                    <button class="action-btn like" aria-label="Me gusta">❤</button>
                </div>
            `;
            grid.appendChild(card);
        });

    } catch (error) {
        console.error("Error cargando perfiles:", error);
        grid.innerHTML = '<p style="color: #ff6b6b; text-align: center; grid-column: 1/-1;">Error al conectar con el servidor</p>';
    }
}

cargarPerfiles();
