const API_URL = "http://localhost:8080/api/usuarios";

// Comprobar si el usuario está logueado
const usuarioLogueado = localStorage.getItem('lovecode_user');
const idUsuarioLogueado = localStorage.getItem('lovecode_user_id');

if (!usuarioLogueado || !idUsuarioLogueado) {
    window.location.href = "login.html"; // Si no está logueado, echarlo al login
} else {
    document.getElementById('user-greeting').textContent = "Hola, " + usuarioLogueado;
}

function cerrarSesion() {
    localStorage.removeItem('lovecode_user');
    localStorage.removeItem('lovecode_user_id');
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
            if (u.id == idUsuarioLogueado) return;

            const card = document.createElement('div');
            card.className = 'profile-card';
            card.innerHTML = `
                <div class="profile-info">
                    <h3>${u.nombre} <span class="status online"></span></h3>
                    <p class="role">📍 ${u.ciudad || 'Sin ciudad'}</p>
                    <p class="bio">"${u.descripcion || 'Sin descripción'}"</p>
                </div>
                <div class="card-actions">
                    <button class="action-btn nope" aria-label="Rechazar" onclick="this.closest('.profile-card').remove()">✗</button>
                    <button class="action-btn like" aria-label="Me gusta" onclick="darLike(${u.id}, this)">❤</button>
                </div>
            `;
            grid.appendChild(card);
        });

    } catch (error) {
        console.error("Error cargando perfiles:", error);
        grid.innerHTML = '<p style="color: #ff6b6b; text-align: center; grid-column: 1/-1;">Error al conectar con el servidor</p>';
    }
}

async function darLike(idReceptor, btn) {
    try {
        const res = await fetch("http://localhost:8080/api/likes", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                idEmisor: parseInt(idUsuarioLogueado),
                idReceptor: idReceptor
            })
        });

        const info = await res.json();

        if (res.ok) {
            // Animación simple de éxito
            btn.style.backgroundColor = "#ff4b2b";
            btn.style.color = "white";
            btn.innerHTML = "✔";
            btn.disabled = true;

            // Si hay MATCH, mostrar notificación especial
            if (info.match) {
                mostrarNotificacionMatch();
            }
            
            // Opcional: Ocultar la tarjeta después de un momento
            setTimeout(() => {
                const card = btn.closest('.profile-card');
                if (card) {
                    card.style.opacity = '0';
                    setTimeout(() => card.remove(), 500);
                }
            }, 1000);

        } else {
            alert(info.error || "Error al dar like");
        }

    } catch (error) {
        console.error("Error al enviar like:", error);
        alert("Error de conexión");
    }
}

function mostrarNotificacionMatch() {
    const overlay = document.createElement('div');
    overlay.className = 'match-overlay';
    overlay.style = `
        position: fixed; top: 0; left: 0; width: 100%; height: 100%;
        background: rgba(0,0,0,0.85); display: flex; align-items: center;
        justify-content: center; z-index: 1000; backdrop-filter: blur(5px);
    `;
    overlay.innerHTML = `
        <div class="match-content" style="background: #1a1c2c; padding: 40px; border-radius: 20px; text-align: center; border: 2px solid #ff4b2b; box-shadow: 0 0 30px rgba(255,75,43,0.3);">
            <h1 style="color: #ff4b2b; margin-bottom: 20px; font-family: 'Fira Code', monospace;">¡IT'S A MATCH! ❤️</h1>
            <p style="color: white; margin-bottom: 30px;">Habéis coincidido en gustos y tecnologías.</p>
            <button onclick="this.closest('.match-overlay').remove()" style="background: #ff4b2b; color: white; border: none; padding: 12px 30px; border-radius: 30px; cursor: pointer; font-weight: bold;">¡Genial!</button>
        </div>
    `;
    document.body.appendChild(overlay);
}

cargarPerfiles();
