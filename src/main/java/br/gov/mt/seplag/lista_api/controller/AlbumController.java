package br.gov.mt.seplag.lista_api.controller;

import br.gov.mt.seplag.lista_api.dto.album.AlbumRequestDTO;
import br.gov.mt.seplag.lista_api.dto.album.AlbumResponseDTO;
import br.gov.mt.seplag.lista_api.service.AlbumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/albuns")
@Tag(name = "Álbuns", description = "Gerenciamento de Álbuns/Discos e relação com Artistas")
public class AlbumController {

    private final AlbumService service;

    public AlbumController(AlbumService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar álbuns (Paginado)", description = "Permite filtrar por nome do artista associado")
    public ResponseEntity<Page<AlbumResponseDTO>> listar(
            @RequestParam(required = false) String artista,
            @PageableDefault(size = 10, sort = "titulo") Pageable pageable) {
        return ResponseEntity.ok(service.listar(artista, pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar álbum por ID")
    public ResponseEntity<AlbumResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo álbum")
    public ResponseEntity<AlbumResponseDTO> criar(@RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar álbum existente")
    public ResponseEntity<AlbumResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AlbumRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
}