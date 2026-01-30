package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.album.AlbumRequestDTO;
import br.gov.mt.seplag.lista_api.dto.album.AlbumResponseDTO;
import br.gov.mt.seplag.lista_api.model.Album;
import br.gov.mt.seplag.lista_api.model.Artista;
import br.gov.mt.seplag.lista_api.repository.AlbumRepository;
import br.gov.mt.seplag.lista_api.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;

    public AlbumService(AlbumRepository albumRepository, ArtistaRepository artistaRepository) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> listar(String filtroArtista, Pageable pageable) {
        Page<Album> page;
        if (filtroArtista != null && !filtroArtista.isBlank()) {
            page = albumRepository.findByArtistaNome(filtroArtista, pageable);
        } else {
            page = albumRepository.findAll(pageable);
        }
        return page.map(AlbumResponseDTO::new);
    }

    @Transactional(readOnly = true)
    public AlbumResponseDTO buscarPorId(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + id));
        return new AlbumResponseDTO(album);
    }

    @Transactional
    public AlbumResponseDTO salvar(AlbumRequestDTO dto) {
        Album album = new Album();
        atualizarDadosAlbum(album, dto);
        
        album = albumRepository.save(album);
        return new AlbumResponseDTO(album);
    }

    @Transactional
    public AlbumResponseDTO atualizar(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + id));
        
        atualizarDadosAlbum(album, dto);
        
        album = albumRepository.save(album);
        return new AlbumResponseDTO(album);
    }

    private void atualizarDadosAlbum(Album album, AlbumRequestDTO dto) {
        album.setTitulo(dto.getTitulo());
        album.setAnoLancamento(dto.getAnoLancamento());

        // Busca e valida os artistas pelo ID
        List<Artista> artistasEncontrados = artistaRepository.findAllById(dto.getArtistasIds());
        
        if (artistasEncontrados.size() != dto.getArtistasIds().size()) {
            throw new RuntimeException("Um ou mais Artistas informados não foram encontrados.");
        }
        
        album.setArtistas(new HashSet<>(artistasEncontrados));
    }
}