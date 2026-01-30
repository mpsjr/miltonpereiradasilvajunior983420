package br.gov.mt.seplag.lista_api.controller;

import br.gov.mt.seplag.lista_api.dto.artista.ArtistaRequestDTO;
import br.gov.mt.seplag.lista_api.dto.artista.ArtistaResponseDTO;
import br.gov.mt.seplag.lista_api.service.ArtistaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/artistas")
@Tag(name = "Artistas", description = "Gerenciamento de Cantores e Bandas")
public class ArtistaController {

    private final ArtistaService service;

    public ArtistaController(ArtistaService service) {
        this.service = service;
    }

    @GetMapping
    @Operation(summary = "Listar artistas", description = "Busca artistas por nome (opcional) com ordenação alfabética (asc/desc)")
    public ResponseEntity<List<ArtistaResponseDTO>> listar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false, defaultValue = "ASC") String ordem) {
        return ResponseEntity.ok(service.listar(nome, ordem));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar artista por ID")
    public ResponseEntity<ArtistaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    @Operation(summary = "Cadastrar novo artista")
    public ResponseEntity<ArtistaResponseDTO> criar(@RequestBody @Valid ArtistaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar artista existente")
    public ResponseEntity<ArtistaResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ArtistaRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }
}