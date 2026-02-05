package br.gov.mt.seplag.lista_api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @Schema(description = "E-mail do usuário cadastrado", example = "admin@seplag.mt.gov.br")
        @NotBlank 
        String email,

        @Schema(description = "Senha de acesso", example = "123")
        @NotBlank 
        String senha
) {}