package br.gov.mt.seplag.lista_api.dto.artista;

import br.gov.mt.seplag.lista_api.model.Artista;
import lombok.Data;

@Data
public class ArtistaResponseDTO {
    private Long id;
    private String nome;

    // Construtor utilitário para converter Entidade -> DTO
    public ArtistaResponseDTO(Artista artista) {
        this.id = artista.getId();
        this.nome = artista.getNome();
    }
}