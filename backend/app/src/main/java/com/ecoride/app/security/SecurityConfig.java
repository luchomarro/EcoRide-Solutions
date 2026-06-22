package com.ecoride.app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SEMANA 6 — Configuración de Spring Security + JWT
 *
 * Reglas de acceso:
 *   PÚBLICO  → POST /api/auth/login (obtener token)
 *   PÚBLICO  → GET  /api/bicicletas y /api/bicicletas/** (lectura libre)
 *   PÚBLICO  → GET/POST /bicicletas/** (vistas Thymeleaf)
 *   PROTEGIDO → POST, PUT, DELETE /api/bicicletas (requiere JWT válido)
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Desactivamos CSRF porque usamos JWT (stateless)
            .csrf(AbstractHttpConfigurer::disable)

            // Sin sesión HTTP — cada request lleva su token
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authorizeHttpRequests(auth -> auth
                // Endpoint de login siempre público
                .requestMatchers("/api/auth/**").permitAll()

                // Lectura de bicicletas pública (GET)
                .requestMatchers(HttpMethod.GET, "/api/bicicletas/**").permitAll()

                // Escritura protegida (POST, PUT, DELETE)
                .requestMatchers(HttpMethod.POST,   "/api/bicicletas/**").authenticated()
                .requestMatchers(HttpMethod.PUT,    "/api/bicicletas/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/bicicletas/**").authenticated()

                // Vistas Thymeleaf públicas
                .requestMatchers("/bicicletas/**").permitAll()

                // Cualquier otra ruta requiere autenticación
                .anyRequest().authenticated()
            )

            // Insertamos el filtro JWT antes del filtro estándar de usuario/contraseña
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Usuarios en memoria para pruebas locales.
     * En producción esto se reemplaza por UserDetailsService contra la tabla 'usuarios'.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
            User.withUsername("admin")
                .password(passwordEncoder().encode("admin123"))
                .roles("ADMIN")
                .build(),
            User.withUsername("operador")
                .password(passwordEncoder().encode("op123"))
                .roles("OPERADOR")
                .build()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}
