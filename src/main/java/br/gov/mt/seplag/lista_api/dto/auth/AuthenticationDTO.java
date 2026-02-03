package br.gov.mt.seplag.lista_api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank String email, 
        @NotBlank String senha
) {}