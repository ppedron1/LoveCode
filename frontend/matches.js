// Comprobar si el usuario está logueado
const usuarioLogueado = localStorage.getItem('lovecode_user');
const usuarioId = localStorage.getItem('lovecode_user_id');

if (!usuarioLogueado || !usuarioId) {
    window.location.href = "login.html";
} else {
    document.getElementById('user-greeting').textContent = "Hola, " + usuarioLogueado;
}

function cerrarSesion() {
    localStorage.removeItem('lovecode_user');
    localStorage.removeItem('lovecode_user_id');
    window.location.href = "index.html";
}

// ==================== CARGAR MATCHES ====================

async function cargarMatches() {
    const lista = document.getElementById('matches-list');

    try {
        const res = await fetch(`http://localhost:8080/api/matches/${usuarioId}`);
        const matches = await res.json();

        if (matches.length === 0) {
            lista.innerHTML = `
                <div class="matches-empty">
                    <div class="empty-icon">💔</div>
                    <h2>Aún no tienes matches</h2>
                    <p>¡Sigue dando likes a perfiles que te gusten!</p>
                    <a href="dashboard.html" class="btn-discover">Descubrir perfiles</a>
                </div>
            `;
            return;
        }

        lista.innerHTML = '';

        matches.forEach(match => {
            const card = document.createElement('div');
            card.className = 'match-card';

            // Generar tags de tecnologías en común
            let techHTML = '';
            if (match.tecnologiasComunes && match.tecnologiasComunes.length > 0) {
                match.tecnologiasComunes.forEach(tech => {
                    techHTML += `<span class="common-tech-tag">${tech}</span>`;
                });
            }

            card.innerHTML = `
                <div class="match-card-left">
                    <div class="match-avatar">💕</div>
                </div>
                <div class="match-card-center">
                    <h3>${match.nombreOtro}</h3>
                    <p class="match-city">📍 ${match.ciudadOtro || 'Sin ciudad'}</p>
                    <p class="match-bio">"${match.descripcionOtro || 'Sin descripción'}"</p>
                    <div class="match-common-techs">
                        <span class="common-label">Tecnologías en común:</span>
                        ${techHTML}
                    </div>
                </div>
                <div class="match-card-right">
                    <span class="match-date">${formatearFecha(match.fechaMatch)}</span>
                </div>
            `;

            lista.appendChild(card);
        });

    } catch (error) {
        console.error("Error cargando matches:", error);
        lista.innerHTML = '<p style="color: #ff6b6b; text-align: center;">Error al conectar con el servidor</p>';
    }
}

function formatearFecha(fechaStr) {
    if (!fechaStr) return '';
    const fecha = new Date(fechaStr);
    return fecha.toLocaleDateString('es-ES', { day: 'numeric', month: 'short', year: 'numeric' });
}

cargarMatches();
