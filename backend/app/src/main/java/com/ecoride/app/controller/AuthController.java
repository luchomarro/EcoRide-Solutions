package com.ecoride.app.controller;

import com.ecoride.app.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * SEMANA 6/7 — Autenticación JWT
 *
 * POST /api/auth/login
 * Body: { "username": "admin", "password": "admin123" }
 * Response: { "token": "eyJ..." }
 *
 * Usuarios de prueba (en memoria, ver SecurityConfig):
 *   admin / admin123    → ROLE_ADMIN
 *   operador / op123    → ROLE_OPERADOR
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Login y generación de tokens JWT")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(
        summary = "Iniciar sesión y obtener un token JWT",
        description = "Usuarios de prueba: admin/admin123 (ADMIN) u operador/op123 (OPERADOR). "
                + "Copia el token devuelto y pégalo en el botón 'Authorize' de Swagger "
                + "para probar los endpoints protegidos del inventario.",
        requestBody = @RequestBody(
            content = @Content(examples = @ExampleObject(
                value = "{ \"username\": \"admin\", \"password\": \"admin123\" }"
            ))
        )
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Login exitoso, token generado"),
        @ApiResponse(responseCode = "401", description = "Usuario o contraseña incorrectos", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody Map<String, String> credentials) {
        try {
            Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    credentials.get("username"),
                    credentials.get("password")
                )
            );

            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            return ResponseEntity.ok(Map.of(
                "token", token,
                "username", userDetails.getUsername(),
                "roles", userDetails.getAuthorities().toString()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Credenciales inválidas"));
        }
    }
}
