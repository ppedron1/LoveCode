package com.LoveCode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UsuarioTest {

    // Prueba 1: Validar email correcto
    @Test
    public void testEmailValido() {
        // Arrange
        Usuario usuario = new Usuario();
        String emailValido = "pablo@example.com";
        
        // Act
        usuario.setEmail(emailValido);
        
        // Assert - verificar que el email cumple el formato básico
        assertTrue(usuario.getEmail().contains("@"), "El email debe contener @");
        assertTrue(usuario.getEmail().contains("."), "El email debe contener un punto");
        assertFalse(usuario.getEmail().startsWith("@"), "El email no puede empezar con @");
    }

    // Prueba 2: Validar contraseña segura
    @Test
    public void testContraseñaSegura() {
        // Arrange
        Usuario usuario = new Usuario();
        String contraseña = "Segura123!";
        
        // Act
        usuario.setPassword(contraseña);
        String passwordGuardada = usuario.getPassword();
        
        // Assert - verificar requisitos de seguridad
        assertTrue(passwordGuardada.length() >= 8, "La contraseña debe tener al menos 8 caracteres");
        assertTrue(passwordGuardada.matches(".*[0-9].*"), "La contraseña debe contener números");
        assertTrue(passwordGuardada.matches(".*[A-Z].*"), "La contraseña debe contener mayúsculas");
    }

    // Prueba 3: Validar nombre no vacío
    @Test
    public void testNombreValido() {
        // Arrange
        Usuario usuario = new Usuario();
        String nombre = "Pablo Pedrón";
        
        // Act
        usuario.setNombre(nombre);
        
        // Assert - verificar que el nombre es válido
        assertNotNull(usuario.getNombre(), "El nombre no puede ser null");
        assertFalse(usuario.getNombre().isEmpty(), "El nombre no puede estar vacío");
        assertTrue(usuario.getNombre().length() >= 3, "El nombre debe tener al menos 3 caracteres");
        assertTrue(usuario.getNombre().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+"), "El nombre solo debe contener letras y espacios");
    }
}
