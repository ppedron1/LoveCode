const API_URL = "http://localhost:8080/api/usuarios";

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
        // Ahora filtramos por ID de usuario logueado en el backend
        const res = await fetch(`${API_URL}?idUsuario=${idUsuarioLogueado}`);
        const usuarios = await res.json();

        if (usuarios.length === 0) {
            grid.innerHTML = '<p style="color: #aaa; text-align: center; grid-column: 1/-1;">No hay nuevos perfiles para descubrir.</p>';
            return;
        }

        grid.innerHTML = '';

        usuarios.forEach(u => {
            const card = crearTarjetaPerfil(u, true);
            grid.appendChild(card);
        });

    } catch (error) {
        console.error("Error cargando perfiles:", error);
        grid.innerHTML = '<p style="color: #ff6b6b; text-align: center; grid-column: 1/-1;">Error al conectar con el servidor</p>';
    }
}

async function cargarLikesDados() {
    const grid = document.getElementById('likes-grid');
    try {
        const res = await fetch(`http://localhost:8080/api/likes/dados/${idUsuarioLogueado}`);
        const usuarios = await res.json();

        if (usuarios.length === 0) {
            grid.innerHTML = '<p style="color: #aaa; text-align: center; grid-column: 1/-1;">No has dado likes todavía.</p>';
            return;
        }

        grid.innerHTML = '';
        usuarios.forEach(u => {
            const card = crearTarjetaPerfil(u, false);
            grid.appendChild(card);
        });
    } catch (error) {
        console.error("Error cargando likes dados:", error);
    }
}

async function cargarMatches() {
    const grid = document.getElementById('matches-grid');
    try {
        const res = await fetch(`http://localhost:8080/api/matches/${idUsuarioLogueado}`);
        const usuarios = await res.json();

        if (usuarios.length === 0) {
            grid.innerHTML = '<p style="color: #aaa; text-align: center; grid-column: 1/-1;">No tienes matches todavía.</p>';
            return;
        }

        grid.innerHTML = '';
        usuarios.forEach(u => {
            const card = crearTarjetaPerfil(u, false, true);
            grid.appendChild(card);
        });
    } catch (error) {
        console.error("Error cargando matches:", error);
    }
}

function crearTarjetaPerfil(u, conAcciones, esMatch = false) {
    const card = document.createElement('div');
    card.className = 'profile-card';
    if (esMatch) card.classList.add('match-card');
    
    let html = `
        <div class="profile-info">
            <h3>${u.nombre}</h3>
            <p class="role">📍 ${u.ciudad || 'Sin ciudad'}</p>
            <p class="bio">"${u.descripcion || 'Sin descripción'}"</p>
            <div class="techs-container">
                ${(u.tecnologias && u.tecnologias !== 'Ninguna' ? u.tecnologias.split(', ') : ['Sin tecnologías']).map(t => `<span class="tech-badge">${t}</span>`).join('')}
            </div>
        </div>
    `;

    if (conAcciones) {
        html += `
            <div class="card-actions">
                <button class="action-btn nope" aria-label="Rechazar" onclick="this.closest('.profile-card').remove()">✗</button>
                <button class="action-btn like" aria-label="Me gusta" onclick="darLike(${u.id}, this)">❤</button>
            </div>
        `;
    } else if (esMatch) {
        html += `
            <div class="card-actions">
                <button class="action-btn chat" aria-label="Chat">💬 Chat</button>
            </div>
        `;
    }

    card.innerHTML = html;
    return card;
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
            btn.style.backgroundColor = "#00c853";
            btn.style.color = "white";
            btn.innerHTML = "✔";
            btn.disabled = true;

            // Si hay MATCH, mostrar notificación especial
            if (info.match) {
                mostrarNotificacionMatch();
                cargarMatches(); // Recargar matches
            }
            
            cargarLikesDados(); // Recargar likes dados

            // Ocultar la tarjeta después de un momento
            setTimeout(() => {
                const card = btn.closest('.profile-card');
                if (card) {
                    card.style.opacity = '0';
                    card.style.transform = 'translateY(-20px)';
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
        justify-content: center; z-index: 1000; backdrop-filter: blur(8px);
        animation: fadeIn 0.5s ease;
    `;
    overlay.innerHTML = `
        <div class="match-content" style="background: #1a1c2c; padding: 40px; border-radius: 24px; text-align: center; border: 2px solid #ff4b2b; box-shadow: 0 0 50px rgba(255,75,43,0.4); transform: scale(0.9); animation: popIn 0.5s cubic-bezier(0.175, 0.885, 0.32, 1.275) forwards;">
            <h1 style="color: #ff4b2b; margin-bottom: 10px; font-size: 3rem; text-shadow: 0 0 10px rgba(255,75,43,0.5);">IT'S A MATCH! ❤️</h1>
            <p style="color: white; font-size: 1.2rem; margin-bottom: 30px;">Habéis coincidido en gustos y tecnologías.</p>
            <button onclick="this.closest('.match-overlay').remove()" style="background: linear-gradient(135deg, #ff4b2b, #ff416c); color: white; border: none; padding: 15px 40px; border-radius: 30px; cursor: pointer; font-weight: bold; font-size: 1.1rem; box-shadow: 0 4px 15px rgba(255,75,43,0.3); transition: transform 0.2s;">¡Genial!</button>
        </div>
    `;
    document.body.appendChild(overlay);
}

// Cargar todo al inicio
cargarPerfiles();
cargarLikesDados();
cargarMatches();
