const URL_BASE = "http://localhost:8080/api/auth";

// Lógica de Registro
async function ejecutarRegistro(e) {
    e.preventDefault();

    const datos = {
        nombre: document.getElementById('reg-nombre').value,
        email: document.getElementById('reg-email').value,
        password: document.getElementById('reg-pass').value,
        ciudad: document.getElementById('reg-ciudad').value
    };

    const res = await fetch(`${URL_BASE}/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(datos)
    });

    const info = await res.json();
    alert(info.mensaje);
}

// Lógica de Login
async function ejecutarLogin(e) {
    e.preventDefault();

    const creds = {
        email: document.getElementById('login-email').value,
        password: document.getElementById('login-pass').value
    };

    const res = await fetch(`${URL_BASE}/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(creds)
    });

    if (res.ok) {
        alert("¡Bienvenido a LoveCode!");
        window.location.href = "index.html";
    } else {
        alert("Fallo en el inicio de sesión");
    }
}