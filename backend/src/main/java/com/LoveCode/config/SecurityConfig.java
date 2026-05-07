package com.LoveCode.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Bean para encriptar contraseñas con BCrypt
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Desactivar la seguridad por defecto de Spring Security
    // Solo usamos BCrypt para hashear contraseñas, no necesitamos protección de endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())           // Desactiva CSRF (necesario para API REST)
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()             // Permite todas las peticiones sin autenticación
            );
        return http.build();
    }
}
