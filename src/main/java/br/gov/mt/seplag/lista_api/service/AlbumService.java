package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.album.AlbumRequestDTO;
import br.gov.mt.seplag.lista_api.dto.album.AlbumResponseDTO;
import br.gov.mt.seplag.lista_api.model.Album;
import br.gov.mt.seplag.lista_api.model.Artista;
import br.gov.mt.seplag.lista_api.model.ImagemAlbum;
import br.gov.mt.seplag.lista_api.repository.AlbumRepository;
import br.gov.mt.seplag.lista_api.repository.ArtistaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final MinioService minioService;

    public AlbumService(AlbumRepository albumRepository, ArtistaRepository artistaRepository, MinioService minioService) {
        this.albumRepository = albumRepository;
        this.artistaRepository = artistaRepository;
        this.minioService = minioService;
    }

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> listar(String filtroArtista, Pageable pageable) {
        Page<Album> page;
        if (filtroArtista != null && !filtroArtista.isBlank()) {
            page = albumRepository.findByArtistaNome(filtroArtista, pageable);
        } else {
            page = albumRepository.findAll(pageable);
        }
        // Converte para DTO populando as URLs assinadas
        return page.map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public AlbumResponseDTO buscarPorId(Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + id));
        return toResponseDTO(album);
    }

    @Transactional
    public AlbumResponseDTO salvar(AlbumRequestDTO dto) {
        Album album = new Album();
        atualizarDadosAlbum(album, dto);
        album = albumRepository.save(album);
        return toResponseDTO(album);
    }

    @Transactional
    public AlbumResponseDTO atualizar(Long id, AlbumRequestDTO dto) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + id));
        atualizarDadosAlbum(album, dto);
        album = albumRepository.save(album);
        return toResponseDTO(album);
    }

    // Método para Upload de Capa
    @Transactional
    public AlbumResponseDTO uploadCapa(Long id, MultipartFile file) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Álbum não encontrado com ID: " + id));

        // 1. Upload físico para o MinIO
        String fileName = minioService.uploadArquivo(file);

        // 2. Salvar referência no Banco
        ImagemAlbum imagem = new ImagemAlbum();
        imagem.setUrl(fileName); // Salva o nome do arquivo, a URL completa é gerada na leitura
        imagem.setAlbum(album);
        
        album.getImagens().add(imagem);
        albumRepository.save(album);

        return toResponseDTO(album);
    }

    private void atualizarDadosAlbum(Album album, AlbumRequestDTO dto) {
        album.setTitulo(dto.getTitulo());
        album.setAnoLancamento(dto.getAnoLancamento());

        List<Artista> artistasEncontrados = artistaRepository.findAllById(dto.getArtistasIds());
        if (artistasEncontrados.size() != dto.getArtistasIds().size()) {
            throw new RuntimeException("Um ou mais Artistas informados não foram encontrados.");
        }
        album.setArtistas(new HashSet<>(artistasEncontrados));
    }

    // Método para converter e assinar URLs
    private AlbumResponseDTO toResponseDTO(Album album) {
        AlbumResponseDTO dto = new AlbumResponseDTO(album);
        
        // Sobrescreve a lista de capas com as URLs pré-assinadas geradas agora
        Set<String> urlsAssinadas = album.getImagens().stream()
                .map(img -> minioService.gerarUrlPreAssinada(img.getUrl()))
                .collect(Collectors.toSet());
        
        dto.setCapasUrls(urlsAssinadas);
        return dto;
    }
}