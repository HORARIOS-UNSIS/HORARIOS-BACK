package com.horarios.horarios_unsis.shared.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI / Swagger
 * 
 * Acceso a Swagger UI:
 * http://localhost:8080/swagger-ui.html
 * http://localhost:8080/swagger-ui/index.html
 * 
 * Acceso a JSON de OpenAPI:
 * http://localhost:8080/v3/api-docs
 * http://localhost:8080/v3/api-docs.yaml
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "HORARIOS-UNSIS API",
        version = "1.0.0",
        description = "API para gestión de horarios, exámenes y sinodales en la Universidad UNSIS",
        contact = @Contact(
            name = "HORARIOS-UNSIS Team",
            url = "https://github.com/HORARIOS-UNSIS/HORARIOS-BACK"
        ),
        license = @License(
            name = "MIT License",
            url = "https://opensource.org/licenses/MIT"
        )
    ),
    servers = {
        @Server(
            url = "http://localhost:8080",
            description = "Local Development"
        ),
        @Server(
            url = "https://api.horarios.unsis.edu.mx",
            description = "Production"
        )
    }
)
@SecurityScheme(
    name = "Bearer",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "JWT token para autenticación",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
    // Configuración de OpenAPI manejada por anotaciones
    // springdoc-openapi automáticamente genera la documentación basada en:
    // - @OpenAPIDefinition
    // - @Operation en controladores
    // - @Schema en modelos
    // - @ApiResponse en respuestas
}
