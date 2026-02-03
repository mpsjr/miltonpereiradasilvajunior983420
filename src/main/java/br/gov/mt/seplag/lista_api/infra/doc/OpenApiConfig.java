package br.gov.mt.seplag.lista_api.infra.doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Processo Seletivo SEPLAG - API Lista Álbuns e Artistas",
        version = "v1",
        description = "API de gerenciamento de Álbuns, Artistas e Regionais com autenticação JWT.",
        contact = @Contact(
            name = "Milton Pereira da Silva Júnior",
            email = "mpsjunior@gmail.com"
        )
    ),
    // Padrão - todos os endpoints exigem autenticação
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth", // nome interno do esquema
    type = SecuritySchemeType.HTTP, // Tipo HTTP
    scheme = "bearer", // O esquema é Bearer
    bearerFormat = "JWT" // O formato é JWT
)
public class OpenApiConfig {
    // apenas para carregar as anotações acima
}