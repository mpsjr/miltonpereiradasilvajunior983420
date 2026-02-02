package br.gov.mt.seplag.lista_api.service;

import br.gov.mt.seplag.lista_api.dto.album.AlbumRequestDTO;
import br.gov.mt.seplag.lista_api.dto.album.AlbumResponseDTO;
import br.gov.mt.seplag.lista_api.model.Album;
import br.gov.mt.seplag.lista_api.model.Artista;
import br.gov.mt.seplag.lista_api.repository.AlbumRepository;
import br.gov.mt.seplag.lista_api.repository.ArtistaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Collections;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {

    @InjectMocks
    private AlbumService albumService;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistaRepository artistaRepository;

    @Mock
    private MinioService minioService;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @Test
    @DisplayName("Deve salvar álbum e notificar via WebSocket")
    void deveSalvarAlbumENotificar() {
        // CENÁRIO
        Long idArtista = 1L;
        
        // DTO de Entrada
        AlbumRequestDTO request = new AlbumRequestDTO();
        request.setTitulo("Album Teste");
        request.setAnoLancamento(2026);
        request.setArtistasIds(Collections.singleton(idArtista));

        // Mocks de Banco
        Artista artistaMock = new Artista();
        artistaMock.setId(idArtista);
        artistaMock.setNome("Artista Teste");

        Mockito.when(artistaRepository.findAllById(request.getArtistasIds()))
                .thenReturn(Collections.singletonList(artistaMock));

        // Mock do Save
        Mockito.when(albumRepository.save(any(Album.class))).thenAnswer(invocation -> {
            Album a = invocation.getArgument(0);
            a.setId(50L); // Simula ID gerado
            return a;
        });

        // AÇÃO
        AlbumResponseDTO response = albumService.salvar(request);

        // VERIFICAÇÃO
        Assertions.assertNotNull(response);
        Assertions.assertEquals(50L, response.getId());
        Assertions.assertEquals("Album Teste", response.getTitulo());

        // Verifica persistência
        Mockito.verify(albumRepository, Mockito.times(1)).save(any(Album.class));

        // Verifica envio WebSocket
        Mockito.verify(messagingTemplate, Mockito.times(1))
                .convertAndSend(eq("/topic/novos-albuns"), any(AlbumResponseDTO.class));

        // Log de Sucesso no Console
        System.out.println("Teste de Cadastro de Álbum: SUCESSO. Álbum ID " + response.getId() + " salvo e notificação WebSocket enviada.");
    }
}