package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.regional.RegionalExternalDTO;
import br.gov.mt.seplag.lista_api.model.Regional;
import br.gov.mt.seplag.lista_api.repository.RegionalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegionalServiceTest {

    private RegionalService service;

    @Mock
    private RegionalRepository repository;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        service = new RegionalService(repository, restTemplateBuilder);
    }

    @Test
    @DisplayName("Deve versionar regional: Inativar antiga e criar nova quando nome mudar")
    void deveVersionarRegionalQuandoNomeMudar() {
        // CENÁRIO
        
        // 1. API Externa (Retorna Nome Novo)
        RegionalExternalDTO dtoExterno = new RegionalExternalDTO();
        dtoExterno.setId(100);
        dtoExterno.setNome("REGIONAL CUIABÁ NOVA");
        RegionalExternalDTO[] responseApi = new RegionalExternalDTO[]{dtoExterno};

        when(restTemplate.getForObject(anyString(), eq(RegionalExternalDTO[].class)))
                .thenReturn(responseApi);

        // 2. Banco (Possui Nome Antigo)
        Regional regionalAntiga = new Regional();
        regionalAntiga.setId(1L);
        regionalAntiga.setIdRegional(100);
        regionalAntiga.setNome("REGIONAL CUIABÁ ANTIGA");
        regionalAntiga.setAtivo(true);

        when(repository.findByAtivoTrue()).thenReturn(List.of(regionalAntiga));

        // AÇÃO
        String resultado = service.sincronizarRegionais();

        // VERIFICAÇÃO
        ArgumentCaptor<Regional> captor = ArgumentCaptor.forClass(Regional.class);
        verify(repository, times(2)).save(captor.capture());

        List<Regional> salvos = captor.getAllValues();

        // Verifica inativação
        Regional antigaSalva = salvos.stream().filter(r -> r.getId() != null).findFirst().orElseThrow();
        assertFalse(antigaSalva.getAtivo(), "A regional antiga deveria ter sido inativada");

        // Verifica criação
        Regional novaSalva = salvos.stream().filter(r -> r.getId() == null).findFirst().orElseThrow();
        assertTrue(novaSalva.getAtivo(), "A nova regional deve nascer ativa");
        assertEquals("REGIONAL CUIABÁ NOVA", novaSalva.getNome());
 
         // Log de Sucesso no Console
        System.out.println("Teste de Versionamento de Regionais: SUCESSO. " + resultado);
    }
}