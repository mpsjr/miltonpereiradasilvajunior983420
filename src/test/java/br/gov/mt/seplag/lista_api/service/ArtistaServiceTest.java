package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.artista.ArtistaRequestDTO;
import br.gov.mt.seplag.lista_api.dto.artista.ArtistaResponseDTO;
import br.gov.mt.seplag.lista_api.model.Artista;
import br.gov.mt.seplag.lista_api.repository.ArtistaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistaServiceTest {

    @InjectMocks
    private ArtistaService service;

    @Mock
    private ArtistaRepository repository;

    @Test
    @DisplayName("Deve salvar artista com sucesso")
    void deveSalvarArtista() {
        // Preparar o DTO de Entrada
        ArtistaRequestDTO requestDTO = new ArtistaRequestDTO();
        requestDTO.setNome("Legião Urbana");

        // 2. Mockar o comportamento do Repository
        when(repository.save(any(Artista.class))).thenAnswer(i -> {
            Artista a = i.getArgument(0);
            a.setId(1L); // Simula o banco gerando ID
            return a;
        });

        // Ação
        ArtistaResponseDTO responseDTO = service.salvar(requestDTO);

        // Verificação
        assertNotNull(responseDTO);
        assertEquals(1L, responseDTO.getId());
        assertEquals("Legião Urbana", responseDTO.getNome());

        verify(repository).save(any(Artista.class));
        
		// Log de Sucesso no Console
		System.out.println("Teste de Cadastro de Artista: SUCESSO.");
    }
}