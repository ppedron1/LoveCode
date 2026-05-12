const API_URL = "http://localhost:8080/api/usuarios";

// Comprobar si el usuario está logueado
const usuarioLogueado = localStorage.getItem('lovecode_user');
const usuarioId = localStorage.getItem('lovecode_user_id');

if (!usuarioLogueado || !usuarioId) {
    window.location.href = "login.html"; // Si no está logueado, echarlo al login
} else {
    document.getElementById('user-greeting').textContent = "Hola, " + usuarioLogueado;
}

function cerrarSesion() {
    localStorage.removeItem('lovecode_user');
    localStorage.removeItem('lovecode_user_id');
    window.location.href = "index.html";
}

// ==================== MAPA DE COLORES DE TECNOLOGÍAS ====================

const techColors = {
    'Java':       { color: '#f89820', class: 'java' },
    'HTML':       { color: '#e44d26', class: 'html' },
    'CSS':        { color: '#2965f1', class: 'css' },
    'JavaScript': { color: '#f7df1e', class: 'javascript' },
    'SQL':        { color: '#f29111', class: 'sql' },
    'Python':     { color: '#ffeb3b', class: 'python' },
    'XML':        { color: '#8cc84b', class: 'xml' }
};

// ==================== CARGAR PERFILES ====================

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
            if (u.id === usuarioId || u.nombre === usuarioLogueado) return;

            const card = document.createElement('div');
            card.className = 'profile-card';
            card.setAttribute('data-user-id', u.id);

            // Generar tags de tecnologías
            let techHTML = '';
            if (u.tecnologias && u.tecnologias.length > 0) {
                techHTML = '<div class="tech-stack">';
                u.tecnologias.forEach(t => {
                    const tc = techColors[t.nombre] || { class: '' };
                    techHTML += `<span class="tech-tag ${tc.class}">${t.nombre}</span>`;
                });
                techHTML += '</div>';
            }

            card.innerHTML = `
                <div class="profile-info">
                    <h3>${u.nombre} <span class="status online"></span></h3>
                    <p class="role">📍 ${u.ciudad || 'Sin ciudad'}</p>
                    ${techHTML}
                    <p class="bio">"${u.descripcion || 'Sin descripción'}"</p>
                </div>
                <div class="card-actions">
                    <button class="action-btn nope" aria-label="Rechazar" onclick="rechazar(this)">✗</button>
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

// ==================== DAR LIKE ====================

async function darLike(idUsuarioRecibe, boton) {
    const card = boton.closest('.profile-card');

    // Deshabilitar botones para evitar doble click
    card.querySelectorAll('.action-btn').forEach(btn => btn.disabled = true);

    try {
        const res = await fetch("http://localhost:8080/api/likes", {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                idUsuarioDa: parseInt(usuarioId),
                idUsuarioRecibe: idUsuarioRecibe
            })
        });

        const info = await res.json();

        // Animación de like
        card.classList.add('liked');

        // Si es match, mostrar el modal
        if (info.status === 'match') {
            setTimeout(() => {
                mostrarModalMatch(info.tecnologiasComunes);
            }, 400);
        }

        // Eliminar la card después de la animación
        setTimeout(() => {
            card.remove();
        }, 500);

    } catch (error) {
        console.error("Error al dar like:", error);
        card.querySelectorAll('.action-btn').forEach(btn => btn.disabled = false);
    }
}

// ==================== RECHAZAR (NOPE) ====================

function rechazar(boton) {
    const card = boton.closest('.profile-card');
    card.classList.add('noped');
    setTimeout(() => {
        card.remove();
    }, 500);
}

// ==================== MODAL DE MATCH ====================

function mostrarModalMatch(tecnologiasComunes) {
    const overlay = document.getElementById('match-overlay');
    const techsContainer = document.getElementById('match-techs');

    // Generar los tags de tecnologías en común
    techsContainer.innerHTML = '';
    tecnologiasComunes.forEach(tech => {
        const tag = document.createElement('span');
        tag.className = 'match-tech-tag';
        tag.textContent = tech;
        techsContainer.appendChild(tag);
    });

    overlay.classList.add('active');
}

function cerrarModalMatch() {
    const overlay = document.getElementById('match-overlay');
    overlay.classList.remove('active');
}

// Cerrar modal con click fuera
document.getElementById('match-overlay').addEventListener('click', function(e) {
    if (e.target === this) {
        cerrarModalMatch();
    }
});

// ==================== INICIALIZAR ====================

cargarPerfiles();
