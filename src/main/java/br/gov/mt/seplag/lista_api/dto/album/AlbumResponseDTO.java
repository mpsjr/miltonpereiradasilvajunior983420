package br.gov.mt.seplag.lista_api.dto.album;

import br.gov.mt.seplag.lista_api.dto.artista.ArtistaResponseDTO;
import br.gov.mt.seplag.lista_api.model.Album;
import br.gov.mt.seplag.lista_api.model.ImagemAlbum;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class AlbumResponseDTO {
    private Long id;
    private String titulo;
    private Integer anoLancamento;
    private Set<String> capasUrls;
    private Set<ArtistaResponseDTO> artistas;

    public AlbumResponseDTO(Album album) {
        this.id = album.getId();
        this.titulo = album.getTitulo();
        this.anoLancamento = album.getAnoLancamento();
        
        // Mapeia a lista de objetos ImagemAlbum para lista de Strings (URLs)
        this.capasUrls = album.getImagens().stream()
                .map(ImagemAlbum::getUrl)
                .collect(Collectors.toSet());
                
        this.artistas = album.getArtistas().stream()
                .map(ArtistaResponseDTO::new)
                .collect(Collectors.toSet());
    }
}