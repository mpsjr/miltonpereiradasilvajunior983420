package br.gov.mt.seplag.lista_api.controller;

import br.gov.mt.seplag.lista_api.model.Regional;
import br.gov.mt.seplag.lista_api.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/regionais")
@Tag(name = "Regionais", description = "Sincronização com API externa")
public class RegionalController {

    private final RegionalService service;

    public RegionalController(RegionalService service) {
        this.service = service;
    }

    @PostMapping("/sincronizacao")
    @Operation(summary = "Disparar Sincronização", description = "Busca dados da API externa e atualiza base local")
    public ResponseEntity<String> sincronizar() {
        String resultado = service.sincronizarRegionais();
        return ResponseEntity.ok(resultado);
    }

    @GetMapping
    @Operation(summary = "Listar Regionais Ativas")
    public ResponseEntity<List<Regional>> listar() {
        return ResponseEntity.ok(service.listarAtivas());
    }
}