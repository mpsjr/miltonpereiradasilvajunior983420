package br.gov.mt.seplag.lista_api.repository;

import br.gov.mt.seplag.lista_api.model.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    // Busca paginada filtrando por parte do nome do artista (JOIN implícito)
    @Query("SELECT alb FROM Album alb JOIN alb.artistas art WHERE LOWER(art.nome) LIKE LOWER(CONCAT('%', :nomeArtista, '%'))")
    Page<Album> findByArtistaNome(@Param("nomeArtista") String nomeArtista, Pageable pageable);
}