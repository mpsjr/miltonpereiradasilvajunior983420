package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.regional.RegionalExternalDTO;
import br.gov.mt.seplag.lista_api.model.Regional;
import br.gov.mt.seplag.lista_api.repository.RegionalRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RegionalService {

    private static final String URL_API_EXTERNA = "https://integrador-argus-api.geia.vip/v1/regionais";
    
    private final RegionalRepository repository;
    private final RestTemplate restTemplate;

    public RegionalService(RegionalRepository repository, RestTemplateBuilder restTemplateBuilder) {
        this.repository = repository;
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Sincroniza a base local com a API Externa.
     * Regras:
		 * 1) Novo no endpoint -> inserir na tabela local.
		 * 2) Não disponível no endpoint -> inativar na tabela local.
		 * 3) Qualquer atributo alterado -> inativar registro anterior e criar novo com nova denominação.
     * * @return Log do que foi feito.
     */
    @Transactional
    public String sincronizarRegionais() {
        // 1. Busca dados da API Externa
        RegionalExternalDTO[] response = restTemplate.getForObject(URL_API_EXTERNA, RegionalExternalDTO[].class);
        List<RegionalExternalDTO> externos = response != null ? Arrays.asList(response) : Collections.emptyList();

        // 2. Busca status atual do banco (apenas Ativos)
        Map<Integer, Regional> atuaisMap = repository.findByAtivoTrue().stream()
                .collect(Collectors.toMap(Regional::getIdRegional, Function.identity()));

        int inseridos = 0;
        int atualizados = 0;
        int inativados = 0;

        // 3. Processar dados externos (Inserção e Atualização)
        Set<Integer> idsProcessados = new HashSet<>();

        for (RegionalExternalDTO ext : externos) {
            idsProcessados.add(ext.getId());
            Regional atual = atuaisMap.get(ext.getId());

            if (atual == null) {
                // Se - Novo no endpoint -> Inserir
                criarNovaRegional(ext);
                inseridos++;
            } else {
                // Já existe: Verificar se mudou o nome
                if (!atual.getNome().equals(ext.getNome())) {
                    // Nome foi alterado -> Inativar antigo e criar novo registro
                    inativarRegional(atual);
                    criarNovaRegional(ext);
                    atualizados++;
                }
            }
        }

        // 4. Processar Ausentes (Inativação)
        // Percorre os que estão no banco mas NÃO vieram na lista externa
        for (Map.Entry<Integer, Regional> entry : atuaisMap.entrySet()) {
            if (!idsProcessados.contains(entry.getKey())) {
                // Caso 2: Ausente -> Inativar
                inativarRegional(entry.getValue());
                inativados++;
            }
        }

        return String.format("Sincronização concluída. Inseridos: %d, Atualizados: %d, Inativados: %d", 
                inseridos, atualizados, inativados);
    }

    @Transactional(readOnly = true)
    public List<Regional> listarAtivas() {
        return repository.findByAtivoTrue();
    }

    private void criarNovaRegional(RegionalExternalDTO dto) {
        Regional nova = new Regional();
        nova.setIdRegional(dto.getId());
        nova.setNome(dto.getNome());
        nova.setAtivo(true);
        repository.save(nova);
    }

    private void inativarRegional(Regional regional) {
        regional.setAtivo(false);
        repository.save(regional);
    }
}