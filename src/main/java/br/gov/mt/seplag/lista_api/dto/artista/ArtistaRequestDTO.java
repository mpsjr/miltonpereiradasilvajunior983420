package br.gov.mt.seplag.lista_api.dto.artista;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistaRequestDTO {
    
    @NotBlank(message = "O nome do artista é obrigatório")
    private String nome;
}