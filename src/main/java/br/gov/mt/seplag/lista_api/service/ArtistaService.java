package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.artista.ArtistaRequestDTO;
import br.gov.mt.seplag.lista_api.dto.artista.ArtistaResponseDTO;
import br.gov.mt.seplag.lista_api.model.Artista;
import br.gov.mt.seplag.lista_api.repository.ArtistaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistaService {

    private final ArtistaRepository repository;

    public ArtistaService(ArtistaRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ArtistaResponseDTO> listar(String termoBusca, String direcaoOrdenacao) {
        // Define a direção da ordenação (padrão ASC se não informado ou inválido)
        Sort.Direction direction = "DESC".equalsIgnoreCase(direcaoOrdenacao) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "nome");

        List<Artista> artistas;
        
        if (termoBusca != null && !termoBusca.isBlank()) {
            artistas = repository.findByNomeContainingIgnoreCase(termoBusca, sort);
        } else {
            artistas = repository.findAll(sort);
        }

        return artistas.stream()
                .map(ArtistaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArtistaResponseDTO buscarPorId(Long id) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado com ID: " + id));
        return new ArtistaResponseDTO(artista);
    }

    @Transactional
    public ArtistaResponseDTO salvar(ArtistaRequestDTO dto) {
        Artista artista = new Artista();
        artista.setNome(dto.getNome());
        
        artista = repository.save(artista);
        return new ArtistaResponseDTO(artista);
    }

    @Transactional
    public ArtistaResponseDTO atualizar(Long id, ArtistaRequestDTO dto) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Artista não encontrado com ID: " + id));
        
        artista.setNome(dto.getNome());
        artista = repository.save(artista);
        
        return new ArtistaResponseDTO(artista);
    }
}