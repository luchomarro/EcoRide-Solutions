package com.ecoride.app.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SEMANA 5/7 — Documentación de API con Swagger/OpenAPI
 *
 * Configura la metadata visible en /swagger-ui.html y agrega
 * el esquema "Bearer Token" para poder probar endpoints protegidos
 * directamente desde la interfaz de Swagger (botón "Authorize").
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI ecoRideOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
            .info(new Info()
                .title("EcoRide Solutions API")
                .description("API REST para la gestión de inventario de bicicletas EcoRide. "
                        + "Proyecto académico — UPN, curso Soluciones Web y Aplicaciones Distribuidas. "
                        + "Alineado al ODS 9 (Industria, innovación e infraestructura).")
                .version("1.0.0")
                .contact(new Contact()
                    .name("EcoRide Solutions")
                    .url("https://github.com/luchomarro/EcoRide-Solutions")))
            .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
            .components(new Components()
                .addSecuritySchemes(securitySchemeName,
                    new SecurityScheme()
                        .name(securitySchemeName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Pega aquí el token obtenido en POST /api/auth/login "
                                + "(sin el prefijo 'Bearer ', Swagger lo agrega automáticamente)")));
    }
}
