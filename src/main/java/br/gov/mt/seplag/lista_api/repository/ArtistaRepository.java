package br.gov.mt.seplag.lista_api.repository;

import br.gov.mt.seplag.lista_api.model.Artista;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistaRepository extends JpaRepository<Artista, Long> {
    
    // Método para busca por nome (contendo) ignorando case, com suporte a ordenação
    List<Artista> findByNomeContainingIgnoreCase(String nome, Sort sort);
}