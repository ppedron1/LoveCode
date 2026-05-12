const API_URL = "http://localhost:8080/api/usuarios";

const usuarioLogueado = localStorage.getItem('lovecode_user');
const idUsuarioLogueado = localStorage.getItem('lovecode_user_id');

if (!usuarioLogueado || !idUsuarioLogueado) {
    window.location.href = "login.html"; // Si no está logueado o falta el ID, echarlo
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
            // Usamos el ID para mayor seguridad
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
            
            // Opcional: Ocultar la tarjeta después de un momento
            setTimeout(() => {
                btn.closest('.profile-card').style.opacity = '0';
                setTimeout(() => btn.closest('.profile-card').remove(), 500);
            }, 1000);

        } else {
            alert(info.error || "Error al dar like");
        }

    } catch (error) {
        console.error("Error al enviar like:", error);
        alert("Error de conexión");
    }
}

cargarPerfiles();
